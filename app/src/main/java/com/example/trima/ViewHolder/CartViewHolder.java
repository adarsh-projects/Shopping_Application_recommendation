package com.example.trima.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trima.Interface.ItemClickListener;
import com.example.trima.R;

public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView  cartProductName, cartProductPrice, cartProductQuantity;
    public ItemClickListener itemClickListener;

    public CartViewHolder(@NonNull View itemView) {
        super(itemView);
        cartProductName = (TextView) itemView.findViewById(R.id.cart_product_name);
        cartProductPrice = (TextView) itemView.findViewById(R.id.cart_product_price);
        cartProductQuantity = (TextView) itemView.findViewById(R.id.cart_product_quantity);
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view, getAdapterPosition(), false);
    }

    public void  setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
}
