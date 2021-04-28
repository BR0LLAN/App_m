package com.example.comp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class BoxAdapter extends RecyclerView.Adapter<BoxAdapter.MyViewHolder> {
    ArrayList<ProductsModel> dataListBox;
    List<String> ord;

    public BoxAdapter(ArrayList<ProductsModel> dataListBox) {
        this.dataListBox = dataListBox;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.box_recycle_single, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        //holder.title_p.setText(ord+"");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore connectDB = FirebaseFirestore.getInstance();
        connectDB.collection("Users").document(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();
                ord = (List<String>) document.get("orders");

                for (String orde : ord){
                    if (orde == dataListBox.get(position).getId_product()){
                        String orderId = dataListBox.get(position).getId_product();

                    }
                    holder.title_p.setText(orde);
                }

        }

        });

    }

    @Override
    public int getItemCount() {
        return dataListBox.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView title_p;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title_p = itemView.findViewById(R.id.b_title_product);
        }
    }
}
