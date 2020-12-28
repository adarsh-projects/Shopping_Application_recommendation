package com.example.trima.Buyer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.trima.R;
import com.example.trima.prevalent.*;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ConfirmFinalOrderActivity extends AppCompatActivity {

    private EditText userName, userHomeaAddress, userCityAddress, userPhone;
    private Button placeOrder;
    private String totalPrice = "";
    private String productId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_final_order);

        totalPrice = getIntent().getStringExtra("TotalPrice");

        userName = (EditText)findViewById(R.id.shippment_name);
        userPhone = (EditText)findViewById(R.id.shippment_phone_number);
        userCityAddress = (EditText)findViewById(R.id.shippment_city_address);
        userHomeaAddress = (EditText)findViewById(R.id.shippment_home_address);

        placeOrder = (Button)findViewById(R.id.oder_placed);

        placeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check();
            }
        });

    }
    public void check(){
        if(TextUtils.isEmpty(userName.getText().toString())){
            Toast.makeText(this, "Please provide your name", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(userPhone.getText().toString())){
            Toast.makeText(this, "Please provide your Phone Number", Toast.LENGTH_SHORT).show();
        } else if(TextUtils.isEmpty(userCityAddress.getText().toString())){
            Toast.makeText(this, "Please provide your city name ", Toast.LENGTH_SHORT).show();
        } else if(TextUtils.isEmpty(userHomeaAddress.getText().toString())){
            Toast.makeText(this, "Please provide home addess", Toast.LENGTH_SHORT).show();
        }else{
            confirmOrder();
        }
    }
    public void confirmOrder(){
        final String saveCurrentTime, saveCurrentDate;

        Calendar calForDate = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calForDate.getTime());
        final DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference()
                .child("Orders")
                .child(prevalent.CurrentOnlineUser.getPhone());

        final HashMap<String, Object> orderMap = new HashMap<>();
        orderMap.put("TotalPrice", totalPrice);
        orderMap.put("Uname", userName.getText().toString());
        orderMap.put("Uphone", userPhone.getText().toString());
        orderMap.put("Uaddress", userHomeaAddress.getText().toString());
        orderMap.put("Ucity", userCityAddress.getText().toString());
        orderMap.put("Data", saveCurrentDate);
        orderMap.put("Time", saveCurrentTime);
        orderMap.put("state", "not shipped");

        orderRef.updateChildren(orderMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    FirebaseDatabase.getInstance().getReference().child("Cart List")
                            .child("User View")
                            .child(prevalent.CurrentOnlineUser.getPhone())
                            .removeValue()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(ConfirmFinalOrderActivity.this, "Your order Placed successfuly", Toast.LENGTH_SHORT).show();
                                        Intent i = new Intent(ConfirmFinalOrderActivity.this, HomeActivity.class);
                                         i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(i);
                                    }
                                }
                            });
                }
            }
        });

    }
}