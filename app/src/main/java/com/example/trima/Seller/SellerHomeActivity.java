package com.example.trima.Seller;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.trima.HomePage.FrontPage;
import com.example.trima.R;
import com.example.trima.ViewHolder.SellerProductViewHolder;
import com.example.trima.model.SellerProduct;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SellerHomeActivity extends AppCompatActivity {


    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private String sellerid;

    private FirebaseAuth mAuth;
    private DatabaseReference productref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_home);
        BottomNavigationView navView = findViewById(R.id.bottom_nav_view);


        mAuth = FirebaseAuth.getInstance();
        sellerid = mAuth.getCurrentUser().getUid();
        productref = FirebaseDatabase.getInstance().getReference().child("Products");

        recyclerView = findViewById(R.id.seller_home_product_list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_add, R.id.navigation_order_list,R.id.navigation_maintain)
                .build();

        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();

                if(id == R.id.navigation_home){
                    Toast.makeText(SellerHomeActivity.this, "Home", Toast.LENGTH_SHORT).show();
                    return true;
                }else if(id == R.id.navigation_add){
                    Intent i = new Intent(SellerHomeActivity.this, SellerProductCategoryActivity.class);
                    startActivity(i);
                    return true;
                }else if(id == R.id.navigation_order_list) {
                    Intent i = new Intent(SellerHomeActivity.this, SellerNewOrderListActivity.class);
                    i.putExtra("sid",sellerid);
                    startActivity(i);
                    return true;
                }else if(id == R.id.navigation_maintain) {
                    final FirebaseAuth mAuth;
                    mAuth = FirebaseAuth.getInstance();
                    mAuth.signOut();
                    Intent i = new Intent(SellerHomeActivity.this, FrontPage.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                    finish();
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<SellerProduct> options = new FirebaseRecyclerOptions.Builder<SellerProduct>()
                .setQuery(productref.orderByChild("SellerId").equalTo(sellerid), SellerProduct.class)
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
                        Intent i = new Intent(SellerHomeActivity.this, SellerProductMaintenceActivity.class);
                        i.putExtra("pid", model.getPid());
                        startActivity(i);
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
}