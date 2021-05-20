package com.example.comp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class BoxFragment extends Fragment {

    private FirebaseFirestore firebaseFirestore;
    private FirebaseFirestore orderId;
    RecyclerView recyclerView_box;
    BoxAdapter adapter;
    ArrayList<ProductsModel> dataListBox;
    TextView count_p;
    private TextView box_p_price;
    ArrayList<String> ord;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_box, container, false);

        recyclerView_box = view.findViewById(R.id.recycle_view_box);
        firebaseFirestore = FirebaseFirestore.getInstance();
        orderId = FirebaseFirestore.getInstance();
        count_p=view.findViewById(R.id.count_product1);
        box_p_price=view.findViewById(R.id.box_p_price);


        dataListBox = new ArrayList<>();
        adapter = new BoxAdapter(dataListBox);
        recyclerView_box.setAdapter(adapter);

        recyclerView_box.setHasFixedSize(true);
        recyclerView_box.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView_box.setAdapter(adapter);


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        orderId.collection("Users").document(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();
                ord = (ArrayList<String>) document.get("orders");

                    firebaseFirestore.collection("Products").get().
                            addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                                    int pri=0;
                                    for (DocumentSnapshot d : list) {
                                        ProductsModel obj = d.toObject(ProductsModel.class);
                                        if (ord.contains(obj.getId_product())) {
                                            dataListBox.add(obj);
                                            count_p.setText(String.valueOf(dataListBox.size()));
                                            pri += Integer.parseInt(obj.getPrice());
                                        }
                                    }
                                    box_p_price.setText(pri+" ");
                                    adapter.notifyDataSetChanged();
                                }
                            });
                }


        });

        return view;
    }
}