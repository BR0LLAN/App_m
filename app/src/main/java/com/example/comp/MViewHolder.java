package com.example.comp;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

class MViewHolder extends RecyclerView.ViewHolder {
    //For search fragment
    ImageView product_imageView_SF;
    TextView title_p_SF;
    TextView price_p_SF;
    ToggleButton addToFavBtn;
    ToggleButton addToBoxBtn;
    //____________________
    //For box fragment
    TextView title_p_BF;
    TextView price_p_BF;
    ImageView product_imageView_BF;
    TextView pro_price_BF;
    Button delete_product_btn;
    ProgressBar progressBar_s;
    //____________________
    //For box fragment
    TextView title_p_FF;
    TextView price_p_FF;
    ImageView product_imageView_FF;
    ToggleButton add_delete_ToFavBtn;
    ToggleButton addToBoxBtn_FF;
    //____________________
    View v;

    public MViewHolder(@NonNull View itemView) {
        super(itemView);
        //For search fragment
        product_imageView_SF=itemView.findViewById(R.id.image_product);
        title_p_SF=itemView.findViewById(R.id.s_title_product);
        price_p_SF=itemView.findViewById(R.id.s_price_product);
        addToFavBtn=itemView.findViewById(R.id.addToFav_btn_img);
        addToBoxBtn=itemView.findViewById(R.id.addToBox_btn_img);
        //____________________
        //For box fragment
        product_imageView_BF=itemView.findViewById(R.id.box_image_product);
        title_p_BF=itemView.findViewById(R.id.b_title_product);
        price_p_BF=itemView.findViewById(R.id.b_price_product);
        pro_price_BF=itemView.findViewById(R.id.box_p_price);
        delete_product_btn=itemView.findViewById(R.id.delete_product_btn);
        progressBar_s=itemView.findViewById(R.id.progress_bar_BF_s);
        //____________________
        //For search fragment
        product_imageView_FF=itemView.findViewById(R.id.image_product_FF);
        title_p_FF=itemView.findViewById(R.id.f_title_product);
        price_p_FF=itemView.findViewById(R.id.f_price_product);
        add_delete_ToFavBtn=itemView.findViewById(R.id.add_delete_ToFav_btn);
        addToBoxBtn_FF=itemView.findViewById(R.id.addToBox_btn);
        //____________________
        v=itemView;
    }
}
