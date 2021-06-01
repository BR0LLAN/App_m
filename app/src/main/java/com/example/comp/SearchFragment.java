package com.example.comp;

import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;


public class SearchFragment extends Fragment {

    FirestoreRecyclerAdapter adapter;
    RecyclerView recyclerViewList;
    StorageReference storageReference;
    EditText searchInput;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        recyclerViewList = view.findViewById(R.id.recycle_view_search);
        searchInput = view.findViewById(R.id.search_line);
        storageReference= FirebaseStorage.getInstance().getReference();


        recyclerViewList.setHasFixedSize(true);
        recyclerViewList.setLayoutManager(new GridLayoutManager(getContext(), 2));

        loadData("");

        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString()!=null)
                {
                    loadData(s.toString());
                }
                else
                {
                    loadData("");
                }

            }
        });

        return view;
    }

    private void loadData(String data) {
        final Query query = FirebaseFirestore.getInstance()
                .collection("Products").orderBy("title_product").startAt(data).endAt(data+"\uf8ff");
        FirestoreRecyclerOptions<ProductsModel> options = new FirestoreRecyclerOptions.Builder<ProductsModel>()
                .setQuery(query, ProductsModel.class).build();
        adapter = new FirestoreRecyclerAdapter<ProductsModel, MViewHolder>(options) {
            @Override
            public void onBindViewHolder(final MViewHolder holder, final int position, final ProductsModel model) {
                holder.title_p_SF.setText(model.getTitle_product());
                holder.price_p_SF.setText(model.getPrice());

                storageReference.child(model.getId_product() + ".png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Glide.with(holder.product_imageView_SF.getContext()).load(uri.toString()).into(holder.product_imageView_SF);
                    }
                });

                holder.addToBoxBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Animation animScale = AnimationUtils.loadAnimation(getContext(), R.anim.scale_btn);
                        v.startAnimation(animScale);
                        Random random = new Random();
                        Map<String, Object> docData = new HashMap<>();
                        docData.put("id_product", model.getId_product());
                        docData.put("title_product", model.getTitle_product());
                        docData.put("price", model.getPrice());
                        docData.put("time_do_order", FieldValue.serverTimestamp());

                        FirebaseFirestore.getInstance()
                                .collection("Users").document(FirebaseAuth.getInstance().getUid())
                                .collection("Orders").document(random.nextInt()+model.getId_product()).set(docData);
                    }
                });

                holder.addToFavBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Animation animScale = AnimationUtils.loadAnimation(getContext(), R.anim.scale_btn);
                        v.startAnimation(animScale);
                        Random random = new Random();
                        Map<String, Object> docData1 = new HashMap<>();
                        docData1.put("id_product", model.getId_product());
                        docData1.put("title_product", model.getTitle_product());
                        docData1.put("price", model.getPrice());
                        docData1.put("time_do_order", FieldValue.serverTimestamp());

                        FirebaseFirestore.getInstance()
                                .collection("Users").document(FirebaseAuth.getInstance().getUid())
                                .collection("Favorites").document(model.getId_product()).set(docData1);
                    }
                });

                /*holder.v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Fragment fr = new SearchFragment();
                        Bundle args = new Bundle();
                        args.putInt("itemKey", );
                        fr.setArguments(args);
                    }
                });*/


            }

            @Override
            public MViewHolder onCreateViewHolder(ViewGroup group, int i) {
                View view = LayoutInflater.from(group.getContext())
                        .inflate(R.layout.search_recycle_single, group, false);

                return new MViewHolder(view);
            }
        };
        adapter.startListening();
        recyclerViewList.setAdapter(adapter);
    }

}
