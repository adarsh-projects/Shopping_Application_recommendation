package com.example.trima.HomePage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.widget.Toast;

import com.example.trima.Admin.AdminHomeActivity;
import com.example.trima.Buyer.HomeActivity;
import com.example.trima.R;
import com.example.trima.Seller.SellerHomeActivity;
import com.example.trima.model.User;
import com.example.trima.prevalent.prevalent;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 3000;
    FirebaseUser auth = FirebaseAuth.getInstance().getCurrentUser();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Paper.init(this);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                String userPhoneKey = Paper.book().read(prevalent.userPhoneKey);
                String userPassword = Paper.book().read(prevalent.userPassword);
                if(userPhoneKey != "" && userPassword != "" && auth == null){
                    if(!TextUtils.isEmpty(userPhoneKey) && !TextUtils.isEmpty(userPassword)){
                        allowAccess(userPhoneKey, userPassword);
                    }else {
                        Intent i = new Intent(MainActivity.this, FrontPage.class);
                        startActivity(i);
                        finish();
                    }
                }
            }

        }, SPLASH_TIME_OUT);

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (auth != null){
            Intent i = new Intent(MainActivity.this, SellerHomeActivity.class);
            startActivity(i);
        }
    }

    private void allowAccess(final String phone, final String Password) {

        final DatabaseReference rootRef;
        rootRef = FirebaseDatabase.getInstance().getReference();
        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(phone.compareTo("1122334455") == 0){
                        if(snapshot.child("Admin").exists()) {
                            //User userData = snapshot.child("Admin").child(phone).getValue(User.class);
                            String s = snapshot.child("Admin").child("Password").getValue().toString();
                            if (s.equals(Password)) {
                                Toast.makeText(MainActivity.this, "admin Logged in successfully", Toast.LENGTH_SHORT).show();
                                //prevalent.CurrentOnlineUser = userData;
                                    Intent i = new Intent(MainActivity.this, AdminHomeActivity.class);
                                    startActivity(i);
                                }
                            }


                    }else {
                     //   Toast.makeText(getApplicationContext(), phone + "==" + Password, Toast.LENGTH_SHORT).show();
                        if(snapshot.child("Users").child(phone).exists()){
                            User userData = snapshot.child("Users").child(phone).getValue(User.class);
                            if(userData.getPhone().equals(phone)){
                                if(userData.getPassword().equals(Password)){
                                    Toast.makeText(MainActivity.this, "Logged in successfully  ", Toast.LENGTH_SHORT).show();
                                    new prevalent(userData);
                                    Intent i = new Intent(MainActivity.this, HomeActivity.class);
                                    startActivity(i);
                                }else{
                                    Toast.makeText(MainActivity.this, "Password wrong", Toast.LENGTH_SHORT).show();
                                }
                            }else{
                                Toast.makeText(MainActivity.this, "Please Register your number!", Toast.LENGTH_SHORT).show();
                            }
                        }else{

                            Toast.makeText(MainActivity.this,
                                    "Account with this " + phone + " number is not exists.",
                                    Toast.LENGTH_SHORT).show();
                            Toast.makeText(MainActivity.this, "Create new account!", Toast.LENGTH_SHORT).show();
                        }


                    }
                }


                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
    }
}