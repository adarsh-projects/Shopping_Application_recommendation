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
import android.widget.TextView;
import android.widget.Toast;

import com.example.trima.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SellerRegistrationActivity extends AppCompatActivity {
    private EditText sellerName, sellerPassword, sellerEmail, sellerPhone, sellerAddress;
    private TextView sellerLogin;
    private Button Register;
    private ProgressDialog loadingBar;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_registration);

        mAuth = FirebaseAuth.getInstance();

        loadingBar = new ProgressDialog(this);
        sellerAddress = (EditText)findViewById(R.id.seller_register_Address);
        sellerEmail = (EditText)findViewById(R.id.seller_register_email);
        sellerPassword = (EditText)findViewById(R.id.seller_register_password);
        sellerName = (EditText)findViewById(R.id.seller_register_name);
        sellerPhone = (EditText)findViewById(R.id.seller_register_phone);

        sellerLogin = (TextView)findViewById(R.id.seller_register_member);
        Register = (Button)findViewById(R.id.seller_register_confirm);


        sellerLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SellerRegistrationActivity.this, SellerLoginActivity.class);
                startActivity(i);
            }
        });

        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerNewSeller();
            }
        });

    }

    private void registerNewSeller() {
        final String name = sellerName.getText().toString();
        final String email = sellerEmail.getText().toString();
        final String password = sellerPassword.getText().toString();
        final String phone = sellerPhone.getText().toString();
        final String address = sellerAddress.getText().toString();

        if(TextUtils.isEmpty(name)){
            Toast.makeText(this, "Please provide your Name", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "Please provide your Email", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(phone)){
            Toast.makeText(this, "Please provide your Phone Number", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(address)){
            Toast.makeText(this, "Please provide your Address", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "Please provide your Password", Toast.LENGTH_SHORT).show();
        }else{

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                registerSeller(name, email, password, phone, address);
                            }else{
                                Toast.makeText(SellerRegistrationActivity.this, "Email is wrong", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }

    }

    private void registerSeller(String name, String email, String password, String phone, String address) {

        loadingBar.setTitle("Creating Account...");
        loadingBar.setMessage("Please wait, while we are checking the credentials ");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        final DatabaseReference sellerRef = FirebaseDatabase.getInstance().getReference();
        String sellerid = mAuth.getCurrentUser().getUid();

        HashMap<String, Object> sellerMap = new HashMap<>();
        sellerMap.put("Name", name);
        sellerMap.put("Email", email);
        sellerMap.put("Phone", phone);
        sellerMap.put("Address", address);
        sellerMap.put("Password", password);
        sellerMap.put("SellerId", sellerid);

        sellerRef.child("Seller Details").child(sellerid).updateChildren(sellerMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    loadingBar.dismiss();
                    Toast.makeText(SellerRegistrationActivity.this, "Your registration successfully", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(SellerRegistrationActivity.this, SellerHomeActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                    finish();
                }
            }
        });

    }
}