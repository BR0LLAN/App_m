package com.example.comp;

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

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.MyViewHolder>{

    ArrayList<ProductsModel> datalist;

    public SearchAdapter(ArrayList<ProductsModel> datalist) {
        this.datalist = datalist;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_recycle_single, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        holder.title_p.setText(datalist.get(position).getTitle_product());
        holder.price_p.setText(datalist.get(position).getPrice() + " ");

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseFirestore connectDB = FirebaseFirestore.getInstance();
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                String uid = user.getUid();

                Map<String, Object> docData = new HashMap<>();
                docData.put("orders", FieldValue.arrayUnion(datalist.get(position).getId_product()));

                connectDB.collection("Users").document(uid).update(docData);
            }
        });
    }

    @Override
    public int getItemCount() {
        return datalist.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView title_p;
        private TextView price_p;
        private ImageView imageView;
        private TextView count_p;
        private ImageView imageViewfav;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title_p = itemView.findViewById(R.id.s_title_product);
            price_p = itemView.findViewById(R.id.s_price_product);
            imageView = itemView.findViewById(R.id.addToBox_btn_img);





        }
    }
}
