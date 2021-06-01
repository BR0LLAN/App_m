package com.example.comp;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;


public class FavoritesFragment extends Fragment {

    FirestoreRecyclerAdapter adapter;
    RecyclerView recyclerViewList_FF;
    StorageReference storageReference;
    TextView tm;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);

        recyclerViewList_FF = view.findViewById(R.id.recycle_view_favorite);
        tm = view.findViewById(R.id.textView12222);
        storageReference= FirebaseStorage.getInstance().getReference();
        recyclerViewList_FF.setHasFixedSize(true);
        recyclerViewList_FF.setLayoutManager(new GridLayoutManager(getContext(), 2));

        loadData();

        return view;
    }

    private void loadData() {
        Query queryForFav = FirebaseFirestore.getInstance()
                .collection("Users").document(FirebaseAuth.getInstance().getUid()).collection("Favorites");

        queryForFav.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if(document.exists()){
                            tm.setVisibility(View.GONE);
                        }
                    }
                }
            }
        });

        FirestoreRecyclerOptions<ProductsModel> options = new FirestoreRecyclerOptions.Builder<ProductsModel>()
                .setQuery(queryForFav, ProductsModel.class)
                .build();
        adapter = new FirestoreRecyclerAdapter<ProductsModel, MViewHolder>(options) {
            @Override
            protected void onBindViewHolder(final MViewHolder holder, final int position, final ProductsModel model) {
                // Load content in recycleview
                holder.title_p_FF.setText(model.getTitle_product());
                holder.price_p_FF.setText(model.getPrice());

                storageReference.child(model.getId_product() + ".png")
                        .getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Glide.with(holder.product_imageView_FF.getContext())
                                .load(uri.toString())
                                .into(holder.product_imageView_FF);
                    }
                });

                holder.addToBoxBtn_FF.setOnClickListener(new View.OnClickListener() {
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

                holder.add_delete_ToFavBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Animation animScale = AnimationUtils.loadAnimation(getContext(), R.anim.scale_btn);
                        v.startAnimation(animScale);
                                        FirebaseFirestore.getInstance()
                                                .collection("Users")
                                                .document(FirebaseAuth.getInstance().getUid())
                                                .collection("Favorites").document(getItem(position).getId_product()).delete();
                    }
                });
            }




            @Override
            public MViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.favorites_recycle_content, parent, false);
                return new MViewHolder(view);
            }
        };
        adapter.startListening();
        recyclerViewList_FF.setAdapter(adapter);
    }
}
