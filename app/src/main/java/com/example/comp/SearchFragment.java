package com.example.comp;

import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;


public class SearchFragment extends Fragment {

    private FirestoreRecyclerAdapter adapter;
    private RecyclerView recyclerViewList;
    FirebaseFirestore db;
    StorageReference storageReference;
    private ImageView image_pr;
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
                holder.title_p.setText(model.getTitle_product());
                holder.price_p.setText(model.getPrice());

                storageReference.child(model.getId_product() + ".png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Glide.with(holder.product_imageView.getContext()).load(uri.toString()).into(holder.product_imageView);
                    }
                });

                holder.addToBoxBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FirebaseFirestore connectDB = FirebaseFirestore.getInstance();
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                        String uid = user.getUid();

                        Map<String, Object> docData = new HashMap<>();
                        docData.put("orders", FieldValue.arrayUnion(model.getId_product()));

                        connectDB.collection("Users").document(uid).update(docData);
                    }
                });

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
