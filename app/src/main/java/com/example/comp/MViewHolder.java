package com.example.comp;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

class MViewHolder extends RecyclerView.ViewHolder {
    ImageView product_imageView;
    TextView title_p;
    TextView price_p;
    ImageView addToFavBtn;
    ImageView addToBoxBtn;
    View v;


    public MViewHolder(@NonNull View itemView) {
        super(itemView);
        product_imageView=itemView.findViewById(R.id.image_product);
        title_p=itemView.findViewById(R.id.s_title_product);
        price_p=itemView.findViewById(R.id.s_price_product);
        addToFavBtn=itemView.findViewById(R.id.addToFav_btn_img);
        addToBoxBtn=itemView.findViewById(R.id.addToBox_btn_img);
        v=itemView;
    }
}
