package com.example.trima.Seller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.trima.R;
import com.example.trima.ViewHolder.adminOrderViewHolder;
import com.example.trima.model.AdminOrder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SellerNewOrderListActivity extends AppCompatActivity {

    private RecyclerView recycleorderList;
    private DatabaseReference orderRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_new_order_list);

        orderRef = FirebaseDatabase.getInstance().getReference().child("Orders");
        recycleorderList = (RecyclerView)findViewById(R.id.admin_order_product_list);
        recycleorderList.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();
        final FirebaseRecyclerOptions<AdminOrder> options = new FirebaseRecyclerOptions.Builder<AdminOrder>()
                .setQuery(orderRef, AdminOrder.class).build();
        FirebaseRecyclerAdapter<AdminOrder, adminOrderViewHolder> adapter =
                new FirebaseRecyclerAdapter<AdminOrder, adminOrderViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull adminOrderViewHolder holder, final int position, @NonNull final AdminOrder model) {
                holder.userName.setText("Name : "+model.getUname());
                holder.userTotalPrice.setText("TotalPrice : " + model.getTotalPrice());
                holder.userPhone.setText("Phone : " + model.getUphone());
                holder.userDateTime.setText("Date : " + model.getData() + "\nTime : " + model.getTime());
                holder.userShippingAddress.setText("address : " + model.getUaddress() + "\n City : " + model.getUcity());
                holder.showproduct.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(SellerNewOrderListActivity.this, SellerUserActivity.class);
                        i.putExtra("sid", getIntent().getStringExtra("sid"));
                        startActivity(i);
                    }
                });

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CharSequence [] charSequences = new CharSequence[]{
                                "Yes",
                                "No"
                        };
                        AlertDialog.Builder builder = new AlertDialog.Builder(SellerNewOrderListActivity.this);
                        builder.setTitle("Have You shipped this order product?");
                        builder.setItems(charSequences, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (i == 0){
                                    String phone1 = getRef(position).getKey();
                                    removeOrder(phone1);
                                    Intent i1 = new Intent(SellerNewOrderListActivity.this, SellerNewOrderListActivity.class);
                                    startActivity(i1);
                                }
                            }
                        });
                        builder.show();
                    }
                });

            }

            @NonNull
            @Override
            public adminOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_layout,parent, false);

                return new adminOrderViewHolder(view);
            }
        };
        recycleorderList.setAdapter(adapter);
        adapter.startListening();
    }

    private void removeOrder(String phone) {
        orderRef.child(phone).removeValue();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(SellerNewOrderListActivity.this, SellerHomeActivity.class);
        startActivity(i);
        finish();
    }
}