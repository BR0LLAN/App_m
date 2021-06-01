package com.example.comp;

import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
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


public class BoxFragment extends Fragment {


    FirestoreRecyclerAdapter adapter;
    RecyclerView recyclerView_box;
    TextView box_p_price;
    TextView count_p_BF;
    TextView end_price_p_BF;
    StorageReference storageReference;
    Button buy_btn;
    TextView tm1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_box, container, false);

        recyclerView_box = view.findViewById(R.id.recycle_view_box);
        storageReference= FirebaseStorage.getInstance().getReference();
        tm1 = view.findViewById(R.id.textView129222);
        buy_btn = view.findViewById(R.id.buy_btn);
        box_p_price=view.findViewById(R.id.box_p_price);
        count_p_BF=view.findViewById(R.id.count_product_BF);
        end_price_p_BF=view.findViewById(R.id.box_p_price);


        recyclerView_box.setHasFixedSize(true);
        recyclerView_box.setLayoutManager(new LinearLayoutManager(getContext()));

        loadOrdUser();

        return view;
    }

    private void loadOrdUser() {

                    Query queryForBOx = FirebaseFirestore.getInstance()
                            .collection("Users").document(FirebaseAuth.getInstance().getUid()).collection("Orders");

        queryForBOx.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if(document.exists()){
                            tm1.setVisibility(View.GONE);
                        }
                    }
                }
            }
        });

                FirestoreRecyclerOptions<ProductsModel> options = new FirestoreRecyclerOptions.Builder<ProductsModel>()
                        .setQuery(queryForBOx, ProductsModel.class)
                        .build();

                adapter = new FirestoreRecyclerAdapter<ProductsModel, MViewHolder>(options) {
                    @Override
                    public void onBindViewHolder(final MViewHolder holder, final int position, final ProductsModel model) {
                        // Load content in recycleview
                        holder.title_p_BF.setText(model.getTitle_product());
                        holder.price_p_BF.setText(Html.fromHtml("<font color='#D4D4BD'>Цена • </font>") + model.getPrice());
                        end_price_p_BF.setText(String.valueOf(Integer.parseInt(model.getPrice())
                                + Integer.parseInt(end_price_p_BF.getText().toString())));

                        FirebaseFirestore.getInstance()
                                .collection("Users")
                                .document(FirebaseAuth.getInstance().getUid())
                                .collection("Orders").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()){
                                    int count=0;
                                    for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()) {
                                        count += 1;
                                    }
                                    count_p_BF.setText(String.valueOf(count));
                                }
                            }
                        });
                        holder.progressBar_s.setVisibility(View.VISIBLE);
                        storageReference.child(model.getId_product() + ".png")
                                .getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Glide.with(holder.product_imageView_BF.getContext())
                                        .load(uri.toString())
                                        .into(holder.product_imageView_BF);
                            }
                        });
                        holder.progressBar_s.setVisibility(View.INVISIBLE);

                        //_________________________________________________________________________
                        holder.delete_product_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Animation animScale = AnimationUtils.loadAnimation(getContext(), R.anim.scale_btn);
                                v.startAnimation(animScale);
                                final String[] id_order = new String[1];
                                FirebaseFirestore.getInstance()
                                        .collection("Users")
                                        .document(FirebaseAuth.getInstance().getUid())
                                        .collection("Orders").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()){
                                                    int i=0;
                                                    for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()) {
                                                        id_order[i] = documentSnapshot.getId();
                                                        i=i-1;
                                                        i++;
                                                    }
                                                    FirebaseFirestore.getInstance()
                                                            .collection("Users")
                                                            .document(FirebaseAuth.getInstance().getUid())
                                                            .collection("Orders").document(String.valueOf(id_order[i])).delete();
                                                    end_price_p_BF.setText(String.valueOf(Integer.parseInt(end_price_p_BF.getText().toString()) - Integer.parseInt(model.getPrice())));

                                                    FirebaseFirestore.getInstance()
                                                            .collection("Users")
                                                            .document(FirebaseAuth.getInstance().getUid())
                                                            .collection("Orders").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                        @Override
                                                        public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                                                            if (task.isSuccessful()){
                                                                int count=0;
                                                                for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()) {
                                                                    count += 1;
                                                                }
                                                                count_p_BF.setText(String.valueOf(count));
                                                            }
                                                        }
                                                    });
                                                }
                                            }
                                        });
                            }
                        });
                        //________________________________________________________________
                        //do buy method
                        buy_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Animation animScale = AnimationUtils.loadAnimation(getContext(), R.anim.scale_btn);
                                v.startAnimation(animScale);

                                FirebaseFirestore.getInstance()
                                        .collection("Users")
                                        .document(FirebaseAuth.getInstance().getUid())
                                        .collection("Orders").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()){
                                            int count=0;
                                            for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()) {
                                                count += 1;
                                                Map<String, Object> complete_buy = new HashMap<>();
                                                complete_buy.put("id_product", documentSnapshot.getData());
                                                complete_buy.put("title_product", "");
                                                complete_buy.put("time_do_order", FieldValue.serverTimestamp());

                                                FirebaseFirestore.getInstance()
                                                        .collection("Users").document(FirebaseAuth.getInstance().getUid())
                                                        .collection("OrderComplete").document(model.getId_product()).set(complete_buy);
                                            }

                                        }
                                    }
                                });
                            }
                        });
                        //________________________________________________________________
                    }

                    @Override
                    public MViewHolder onCreateViewHolder(ViewGroup group, int i) {
                        View view = LayoutInflater.from(group.getContext())
                                .inflate(R.layout.box_recycle_single, group, false);
                        return new MViewHolder(view);
                    }
                };
                adapter.startListening();
                recyclerView_box.setAdapter(adapter);

            }


    }
