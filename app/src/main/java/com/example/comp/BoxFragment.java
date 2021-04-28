package com.example.comp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class BoxFragment extends Fragment {

    private FirebaseFirestore firebaseFirestore;
    RecyclerView recyclerView_box;
    BoxAdapter adapter;
    ArrayList<ProductsModel> dataListBox;
    TextView fff;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_box, container, false);

        recyclerView_box = view.findViewById(R.id.recycle_view_box);
        firebaseFirestore = FirebaseFirestore.getInstance();
        fff=view.findViewById(R.id.textView8);

        dataListBox = new ArrayList<>();
        adapter = new BoxAdapter(dataListBox);
        recyclerView_box.setAdapter(adapter);

        recyclerView_box.setHasFixedSize(true);
        recyclerView_box.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView_box.setAdapter(adapter);

        firebaseFirestore.collection("Products").get().
                addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot d : list) {
                            ProductsModel obj=d.toObject(ProductsModel.class);
                            dataListBox.add(obj);
                        }
                        adapter.notifyDataSetChanged();
                    }
                });


        return view;
    }
}