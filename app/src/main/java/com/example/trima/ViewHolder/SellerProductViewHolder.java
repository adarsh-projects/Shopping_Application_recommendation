package com.example.trima.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trima.Interface.ItemClickListener;
import com.example.trima.R;

public class SellerProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView sproductName, sproductDescription, sproductPrice, sproductStatus;
    public ItemClickListener itemClickListener;
    public ImageView sproductImage;

    public SellerProductViewHolder(@NonNull View itemView) {
        super(itemView);
        sproductImage = (ImageView)itemView.findViewById(R.id.seller_product_view_image);
        sproductName = (TextView)itemView.findViewById(R.id.seller_product_view_name);
        sproductDescription = (TextView)itemView.findViewById(R.id.seller_product_view_description);
        sproductPrice = (TextView)itemView.findViewById(R.id.seller_product_view_price);
        sproductStatus = (TextView)itemView.findViewById(R.id.seller_product_approved);
    }


    public void setItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }
    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view,getAdapterPosition(),false);
    }
}
