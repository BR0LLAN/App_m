package com.example.comp;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BoxAdapter extends RecyclerView.Adapter<BoxAdapter.MyViewHolder> {
    ArrayList<ProductsModel> dataListBox;
    BoxAdapter adapter;

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
        if (!dataListBox.isEmpty()) {
            holder.title_p.setText(dataListBox.get(position).getTitle_product());
            holder.price_p.setText(Html.fromHtml("<font color='#D4D4BD'>Цена • </font>") + dataListBox.get(position).getPrice()+"");
        }
        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseFirestore connectDB = FirebaseFirestore.getInstance();
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                String uid = user.getUid();

                Map<String, Object> docData = new HashMap<>();
                docData.put("orders", FieldValue.arrayRemove(dataListBox.get(position).getId_product()));

                connectDB.collection("Users").document(uid).update(docData);
            }

        });adapter = new BoxAdapter(dataListBox);
        adapter.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return dataListBox.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView title_p;
        private TextView price_p;
        ImageView deleteBtn;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title_p = itemView.findViewById(R.id.b_title_product);
            price_p = itemView.findViewById(R.id.b_price_product);
            deleteBtn = itemView.findViewById(R.id.btn_delete_p);

        }
    }
}
