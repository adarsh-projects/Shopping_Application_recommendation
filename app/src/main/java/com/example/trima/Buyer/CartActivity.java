package com.example.trima.Buyer;

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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trima.R;
import com.example.trima.ViewHolder.CartViewHolder;
import com.example.trima.prevalent.*;
import com.example.trima.model.Cart;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CartActivity extends AppCompatActivity {

    private RecyclerView productListInCart;
    private RecyclerView.LayoutManager layoutManger;
    private Button nextProcess;
    private TextView totalAmout, msg1;
    private String productId = "";
    private int overTotalPrice = 0;
    private DatabaseReference productref;
    private int n = 0, v = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        productListInCart = findViewById(R.id.product_list);
        productListInCart.setHasFixedSize(true);
        layoutManger = new LinearLayoutManager(this);
        productListInCart.setLayoutManager(layoutManger);

        productref = FirebaseDatabase.getInstance().getReference().child("Products");

        nextProcess = findViewById(R.id.next_product);
        totalAmout = findViewById(R.id.total_cart_amount);
        msg1 = (TextView)findViewById(R.id.confirmation_of_order);


        nextProcess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(overTotalPrice == 0){
                    Toast.makeText(CartActivity.this, "Please select product first", Toast.LENGTH_SHORT).show();
                }else{
                    Intent i = new Intent(CartActivity.this, ConfirmFinalOrderActivity.class);
                    i.putExtra("TotalPrice", String.valueOf(overTotalPrice));
                    startActivity(i);
                    finish();
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        CheckOrderState();
        final DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference().child("Cart List");

        FirebaseRecyclerOptions<Cart> options = new FirebaseRecyclerOptions.Builder<Cart>()
                .setQuery(cartRef.child("User View").child(prevalent.CurrentOnlineUser.getPhone()).child("Products"), Cart.class).build();

        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter = new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull final Cart model) {
                holder.cartProductName.setText( "Name :-" + model.getPname());
                holder.cartProductPrice.setText("Price :-" + model.getPrice());
                holder.cartProductQuantity.setText("Quantity :-" + model.getQuantity());
                productId = model.getPid();
                int v1 = Integer.parseInt(model.getPrice());
                int n1 = model.getQuantity();
                int oneTypeProductPrice = v1 * n1;
                overTotalPrice = overTotalPrice + oneTypeProductPrice;

                totalAmout.setText("Total Price :-" + String.valueOf(overTotalPrice) );

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CharSequence [] option = new CharSequence[]{
                                "Edit",
                                "Remove"
                        };
                        AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);
                        builder.setTitle("Cart Option:");
                        builder.setItems(option, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (i == 0){
                                    cartRef.child("User View").child(prevalent.CurrentOnlineUser.getPhone())
                                            .child("Products").child(model.getPid()).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            n =  Integer.valueOf(String.valueOf(snapshot.child("quantity").getValue()));
                                            productref.child(productId).addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    v = Integer.valueOf(String.valueOf(snapshot.child("ProductQuantity").getValue()));
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });
                                            productref.child(productId).child("ProductQuantity").setValue((v+n));
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                    Intent intent = new Intent(CartActivity.this, ProductDetailsActivity.class);
                                    intent.putExtra("pid", productId);
                                    startActivity(intent);
                                }
                                if(i == 1){
                                    cartRef.child("User View").child(prevalent.CurrentOnlineUser.getPhone())
                                            .child("Products").child(model.getPid()).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            n =  Integer.valueOf(String.valueOf(snapshot.child("quantity").getValue()));
                                            productref.child(productId).addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    v = Integer.valueOf(String.valueOf(snapshot.child("ProductQuantity").getValue()));
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });
                                            productref.child(productId).child("ProductQuantity").setValue((v+n));
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                    cartRef.child("User View").child(prevalent.CurrentOnlineUser.getPhone())
                                            .child("Products").child(model.getPid()).removeValue()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()){
                                                        Toast.makeText(CartActivity.this, "Product is removed from your cart List", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });

                                    //Admin panel status is remaining.
                                }
                            }
                        });
                        builder.show();
                    }
                });
            }

            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item_layout, parent, false);
                CartViewHolder holder = new CartViewHolder(view);
                return holder;
            }
        };
        productListInCart.setAdapter(adapter);
        adapter.startListening();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(CartActivity.this, HomeActivity.class);
        startActivity(i);
        finish();
    }
    public void CheckOrderState(){
        DatabaseReference orderRef;
        orderRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(prevalent.CurrentOnlineUser.getPhone());
        orderRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String shippingState = snapshot.child("state").getValue().toString();
                    String username = snapshot.child("Uname").getValue().toString();
                    if(shippingState.equals("shipped")){
                        totalAmout.setText("Dear" + username + "\n Order is shipped successfully");
                        productListInCart.setVisibility(View.GONE);
                        msg1.setVisibility(View.VISIBLE);
                        msg1.setText("Congratulation, your final order has been shipped successfully. Soon you will received your order at your door step");
                        nextProcess.setVisibility(View.GONE);
                        Toast.makeText(CartActivity.this, "You can purchase more product, once you received your first order", Toast.LENGTH_SHORT).show();
                    }else if(shippingState.equals("not shipped")){
                        totalAmout.setText("Shipping state : " + shippingState);
                        productListInCart.setVisibility(View.GONE);
                        msg1.setVisibility(View.VISIBLE);
                        nextProcess.setVisibility(View.GONE);
                        Toast.makeText(CartActivity.this, "You can purchase more product, once you received your first order", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
    
            }
        });
    }
}