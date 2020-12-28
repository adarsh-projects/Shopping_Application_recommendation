package com.example.trima.Seller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.trima.Buyer.HomeActivity;
import com.example.trima.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class SellerProductMaintenceActivity extends AppCompatActivity {

    private Button applychanges, deleteProduct;
    private ImageView productImage;
    private EditText productName, productPrice, productDescription;
    private String productId = "";
    private DatabaseReference productRef;
    private ProgressDialog loading;
    private String category, description, price, pname, downloadImageurl;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_product_maintence);
        mAuth = FirebaseAuth.getInstance();
        productId = getIntent().getStringExtra("pid");
        productRef = FirebaseDatabase.getInstance().getReference().child("Products");

        loading = new ProgressDialog(this);

        applychanges = (Button)findViewById(R.id.admin_apply_product_change);
        deleteProduct = (Button)findViewById(R.id.admin_delete_product);

        productImage = (ImageView)findViewById(R.id.admin_product_image);
        productName = (EditText)findViewById(R.id.admin_product_name);
        productDescription = (EditText)findViewById(R.id.admin_product_description);
        productPrice = (EditText)findViewById(R.id.admin_product_price);

        productRef.child(productId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String saveCurrentTime, saveCurrentDate;
                Calendar calForDate = Calendar.getInstance();
                SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
                saveCurrentDate = currentDate.format(calForDate.getTime());

                SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
                saveCurrentTime = currentDate.format(calForDate.getTime());

                if(snapshot.exists()){
                    category = snapshot.child("Category").getValue().toString();
                    productName.setText(snapshot.child("ProductName").getValue().toString());
                    productPrice.setText(snapshot.child("Price").getValue().toString());
                    productDescription.setText(snapshot.child("Description").getValue().toString());
                    downloadImageurl = snapshot.child("Image").getValue().toString();
                    Picasso.get().load(snapshot.child("Image").getValue().toString()).into(productImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        deleteProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                productRef.child(productId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Intent i = new Intent(SellerProductMaintenceActivity.this, HomeActivity.class);
                            startActivity(i);
                            finish();
                            Toast.makeText(SellerProductMaintenceActivity.this, "Product Removed successfully", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        applychanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ValidateProductData();
            }
        });

    }


    private void ValidateProductData() {
        description = productDescription.getText().toString();
        price = productPrice.getText().toString();
        pname = productName.getText().toString();
        if(TextUtils.isEmpty(description)){
            Toast.makeText(this, "Please write product description", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(price)){
            Toast.makeText(this, "Please write product price", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(pname)){
            Toast.makeText(this, "Please write product name", Toast.LENGTH_SHORT).show();
        }else{
            storeProductDetails();
        }
    }

    private void storeProductDetails() {

        loading.setTitle("Applying Changes..");
        loading.setMessage("Dear Admin, please wait applying chnages...");
        loading.setCanceledOnTouchOutside(false);
        loading.show();


        String saveCurrentTime, saveCurrentDate;
        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentDate.format(calForDate.getTime());

        String productRandomKey = saveCurrentDate + saveCurrentTime;
        productRef.child(productId).removeValue();
        HashMap<String, Object> productInfo = new HashMap<>();
        productInfo.put("Pid", productRandomKey);
        productInfo.put("Date", saveCurrentDate);
        productInfo.put("Time", saveCurrentTime);
        productInfo.put("Description", description);
        productInfo.put("Image", downloadImageurl);
        productInfo.put("Category", category);
        productInfo.put("Price", price);
        productInfo.put("ProductState", "Approved");
        productInfo.put("ProductName", pname);
        productInfo.put("SellerId",mAuth.getCurrentUser().getUid());

        productRef.child(productRandomKey).updateChildren(productInfo)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            loading.dismiss();
                            Toast.makeText(SellerProductMaintenceActivity.this, "Product is added Successfully", Toast.LENGTH_SHORT).show();
                        }else{
                            loading.dismiss();
                            String message = task.getException().toString();
                            Toast.makeText(SellerProductMaintenceActivity.this, "Error : " + message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(SellerProductMaintenceActivity.this, SellerHomeActivity.class);
        startActivity(i);
    }
}