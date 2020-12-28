package com.example.trima.Buyer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.trima.R;
import com.example.trima.model.Product;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import com.example.trima.prevalent.*;

import org.json.JSONArray;

public class ProductDetailsActivity extends AppCompatActivity {
    private FloatingActionButton addToCart;
    private ImageView productImage;
    private ElegantNumberButton numberButton;
    private RatingBar ratingBar;
    private TextView productPrice, productName, productDescription, pstock;
    private String productId = "", state = "Normal";
    private String sellerId = "", category = "";
    private DatabaseReference productref ;
    private int num = 0, ratingOnProduct = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        productId = getIntent().getStringExtra("pid");
        productref = FirebaseDatabase.getInstance().getReference().child("Products");

        ratingBar = (RatingBar)findViewById(R.id.product_rating_bar);
        addToCart = (FloatingActionButton)findViewById(R.id.add_product_to_cart);
        numberButton = (ElegantNumberButton)findViewById(R.id.product_count);
        productImage = (ImageView)findViewById(R.id.product_image_details);
        pstock = (TextView)findViewById(R.id.product_stock);
        productName = (TextView)findViewById(R.id.product_name);
        productDescription = (TextView)findViewById(R.id.product_description);
        productPrice = (TextView)findViewById(R.id.product_price);

        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(state.equals("Order Shipped") || state.equals("Order Placed")){
                    Toast.makeText(ProductDetailsActivity.this, "you can add purchase more products, once your order is shipped or confirmed", Toast.LENGTH_SHORT).show();
                }else{
                    addingToCart();
                }
            }
        });
        getProductDetails();
    }

    private void addingToCart() {
        num = Integer.parseInt(numberButton.getNumber());
        productref.child(productId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int  total = Integer.parseInt(String.valueOf(snapshot.child("ProductQuantity").getValue()));
                if (total > num){
                    num = total - num;
                    finalOrderPlaced(num);
                }else{
                    Toast.makeText(ProductDetailsActivity.this, "Sorry for inconvenience we didn't have that much of product right know.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void finalOrderPlaced(int n) {
        ratingOnProduct = (int)ratingBar.getRating();
        String saveCurrentTime, saveCurrentDate;
        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calForDate.getTime());

        final DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference().child("Cart List");
        final HashMap<String, Object> cartMap = new HashMap<>();
        cartMap.put("Pid", productId);
        cartMap.put("Pname", productName.getText().toString());
        cartMap.put("Price", productPrice.getText().toString());
        cartMap.put("Data", saveCurrentDate);
        cartMap.put("Time", saveCurrentTime);
        cartMap.put("SellerId", sellerId);
        cartMap.put("quantity", n);
        cartMap.put("Discount", "");
        cartRef.child("User View").child(prevalent.CurrentOnlineUser.getPhone())
                .child("Products").child(productId).updateChildren(cartMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    cartRef.child("Seller View")
                            .child("Products").child(productId).updateChildren(cartMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Intent i = new Intent(ProductDetailsActivity.this, HomeActivity.class);
                                startActivity(i);
                            }
                        }
                    });
                }
            }
        });
    }


    public void onRecommend(View view){
        int rating = ratingOnProduct;

        String [] genre = new String[]{"Shirt", "Pant", "Girl_Shorts", "Men_Shorts", "T-Shirts", "Sports_TShirt",
                "FemaleDress", "Sweater", "Glasses", "PursBag", "Hats", "Shoes", "Laptop", "Mobile", "Watches"};

    }

    private void getProductDetails() {
        DatabaseReference productRef = FirebaseDatabase.getInstance().getReference().child("Products");
        productRef.child(productId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    Product product = snapshot.getValue(Product.class);
                    Picasso.get().load(product.getImage()).into(productImage);
                    productName.setText(product.getProductName());
                    productDescription.setText(product.getDescription());
                    productDescription.setLines(1);
                    productPrice.setText(product.getPrice());
                    sellerId = product.getSellerId();
                    category = product.getCategory();
                    int y = product.getProductQuantity();
                    if(y > 0) {
                        pstock.setText("In Stock");
                    }else{
                        pstock.setText("OutOf Stock");
                        pstock.setTextColor(Color.RED);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        CheckOrderState();
    }

    public void CheckOrderState(){
        DatabaseReference orderRef;
        orderRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(prevalent.CurrentOnlineUser.getPhone());
        orderRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String shippingState = snapshot.child("state").getValue().toString();
                    if(shippingState.equals("shipped")){
                        state = "Order Shipped";
                    }else if(shippingState.equals("not shipped")){
                        state = "Order Placed";
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}