package com.example.trima.ViewHolder;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trima.R;

import org.w3c.dom.Text;

public class adminOrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView userName, userPhone, userDateTime, userShippingAddress, userTotalPrice;
    public Button  showproduct;

    public adminOrderViewHolder(@NonNull View itemView) {
        super(itemView);
        userName = itemView.findViewById(R.id.order_user_name);
        userPhone = itemView.findViewById(R.id.order_phone_number);
        userDateTime = itemView.findViewById(R.id.order_dateTime);
        userShippingAddress = itemView.findViewById(R.id.order_address);
        userTotalPrice = itemView.findViewById(R.id.order_total_price);
        showproduct = itemView.findViewById(R.id.show_product);
    }

    @Override
    public void onClick(View view) {

    }
}
