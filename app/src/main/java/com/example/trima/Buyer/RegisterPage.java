package com.example.trima.Buyer;

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
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class RegisterPage extends AppCompatActivity {
    private Button register;
    private EditText username, phone, confirm_password, password;
    private ProgressDialog loading;
    private TextView memberlogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);
        register = (Button)findViewById(R.id.register_member);
        username = (EditText)findViewById(R.id.register_username);
        phone = (EditText)findViewById(R.id.register_phone);
        password = (EditText)findViewById(R.id.register_password);
        confirm_password = (EditText)findViewById(R.id.register_confirm_password);

        memberlogin = (TextView)findViewById(R.id.register_remember_me);
        loading = new ProgressDialog(this);

        memberlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(RegisterPage.this, LoginPage.class);
                startActivity(i);
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAccount();
            }
        });
    }
    public void createAccount(){
        String u_name = username.getText().toString();
        String u_phone = phone.getText().toString();
        String u_password = password.getText().toString();
        String u_con_password = confirm_password.getText().toString();
        if(TextUtils.isEmpty(u_name)){
            Toast.makeText(this, "Please enter a username", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(u_phone)){
            Toast.makeText(this, "Please enter a phone number", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(u_password)){
            Toast.makeText(this, "Please enter a password", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(u_con_password)){
            Toast.makeText(this, "Please confirm a password", Toast.LENGTH_SHORT).show();
        }else {
            if(u_password.equals(u_con_password)){
                loading.setTitle("Creating Account...");
                loading.setMessage("Please wait, while we are checking the credentials ");
                loading.setCanceledOnTouchOutside(false);
                loading.show();

                if(u_phone.length() == 10){
                    ValidatePhoneNUmber(u_name, u_phone, u_password, u_con_password);
                    loading.dismiss();
                }else {
                    Toast.makeText(this, "Please enter valid number", Toast.LENGTH_SHORT).show();
                    loading.dismiss();
                }
            }else{
                Toast.makeText(this, "Password not matched", Toast.LENGTH_SHORT).show();
            }
        }
    }
    
    private void ValidatePhoneNUmber(final String u_name, final String u_phone, final String u_password, String u_con_password) {
        final DatabaseReference rootRef;
        rootRef = FirebaseDatabase.getInstance().getReference();
        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!(dataSnapshot.child("Users").child(u_phone).exists())){
                    HashMap<String, Object> hm = new HashMap<>();
                    hm.put("UserName", u_name);
                    hm.put("Phone", u_phone);
                    hm.put("Password", u_password);

                    rootRef.child("Users").child(u_phone).updateChildren(hm).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(RegisterPage.this, "Congratulation, your account created. ", Toast.LENGTH_SHORT).show();
                                loading.dismiss();
                                Intent i = new Intent(getApplicationContext(), LoginPage.class);
                                startActivity(i);
                            }else{
                                Toast.makeText(RegisterPage.this, "Network Error: Please try again after some time ", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else{
                    Toast.makeText(RegisterPage.this, "This " + phone + "already exists.", Toast.LENGTH_SHORT).show();
                    loading.dismiss();
                    Intent i = new Intent(getApplicationContext(), LoginPage.class);
                    startActivity(i);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}