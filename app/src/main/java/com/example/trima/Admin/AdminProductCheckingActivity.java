package com.example.trima.Admin;

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

import com.example.trima.Interface.ItemClickListener;
import com.example.trima.R;
import com.example.trima.Seller.SellerHomeActivity;
import com.example.trima.Seller.SellerProductListActivity;
import com.example.trima.Seller.SellerProductMaintenceActivity;
import com.example.trima.ViewHolder.ProductViewHolder;
import com.example.trima.ViewHolder.SellerProductViewHolder;
import com.example.trima.model.Product;
import com.example.trima.model.SellerProduct;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class AdminProductCheckingActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private DatabaseReference productref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_product_checking);

        productref = FirebaseDatabase.getInstance().getReference().child("Products");

        recyclerView = findViewById(R.id.admin_product_list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<SellerProduct> options = new FirebaseRecyclerOptions.Builder<SellerProduct>()
                .setQuery(productref.orderByChild("ProductState").equalTo("Not Approved"), SellerProduct.class)
                .build();
        FirebaseRecyclerAdapter<SellerProduct, SellerProductViewHolder> adapter = new FirebaseRecyclerAdapter<SellerProduct, SellerProductViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull SellerProductViewHolder holder, int position, @NonNull final SellerProduct model) {

                holder.sproductName.setText(model.getProductName());
                holder.sproductPrice.setText(model.getDescription());
                holder.sproductPrice.setText(model.getPrice());
                holder.sproductStatus.setText(model.getProductState());
                Picasso.get().load(model.getImage()).into(holder.sproductImage);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final String productId = model.getPid();
                        CharSequence[] option = new CharSequence[]{
                                "Yes",
                                "No"
                        };
                        AlertDialog.Builder builder = new AlertDialog.Builder(AdminProductCheckingActivity.this);
                        builder.setTitle("Do you want to approved this Product. Are you sure");
                        builder.setItems(option, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if(i == 0){
                                    changeProductState(productId);
                                }
                            }
                        });
                        builder.show();
                    }
                });

            }

            @NonNull
            @Override
            public SellerProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.seller_product_list, parent, false);
                SellerProductViewHolder holder = new SellerProductViewHolder(view);

                return holder;
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    private void changeProductState(String productId) {
        productref.child(productId).child("ProductState").setValue("Approved").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(AdminProductCheckingActivity.this, "Product Approved successfully", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(AdminProductCheckingActivity.this, "Product not Approved", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(AdminProductCheckingActivity.this,AdminHomeActivity.class);
        startActivity(i);
        finish();
    }
}
