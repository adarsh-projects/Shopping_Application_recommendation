package com.example.trima.ViewHolder;

import android.content.ClipData;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trima.Interface.ItemClickListener;
import com.example.trima.R;

public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView productName, productDescription, productPrice, pdiscount, pquantity;
    public ItemClickListener itemClickListener;
    public ImageView productImage;

    public ProductViewHolder(@NonNull View itemView) {
        super(itemView);
        productImage = (ImageView)itemView.findViewById(R.id.product_view_image);
        productName = (TextView)itemView.findViewById(R.id.product_view_name);
        productDescription = (TextView)itemView.findViewById(R.id.product_view_description);
        productPrice = (TextView)itemView.findViewById(R.id.product_view_price);
        pdiscount = (TextView)itemView.findViewById(R.id.product_discount);
        pquantity = (TextView)itemView.findViewById(R.id.product_availability);

    }

    public void setItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view, getAdapterPosition(), false);
    }
}
