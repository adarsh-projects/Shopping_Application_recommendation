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

import com.example.trima.Admin.AdminHomeActivity;
import com.example.trima.Seller.SellerProductCategoryActivity;
import com.example.trima.R;
import com.example.trima.model.User;
import com.example.trima.prevalent.prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class LoginPage extends AppCompatActivity {
    private Button login;
    private EditText phone, password;
    private ProgressDialog loading;
    private TextView createAccount, admin, ForgetPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        phone = (EditText)findViewById(R.id.login_phone);
        password = (EditText)findViewById(R.id.login_password);

        login = (Button)findViewById(R.id.login_member);


        Paper.init(this);
        ForgetPassword = (TextView)findViewById(R.id.login_password_forgot);
        ForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginPage.this, PasswordResetActivity.class);
                i.putExtra("check", "login");
                startActivity(i);
            }
        });
        loading = new ProgressDialog(this);

        // after clicking on create account is send to RegisterPage
        createAccount = (TextView)findViewById(R.id.login_createAccount);
        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginPage.this, RegisterPage.class);
                startActivity(i);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ValidateNumber();
            }
        });
    }

    private void ValidateNumber() {
        String u_phone = phone.getText().toString();
        String u_password = password.getText().toString();
        if(TextUtils.isEmpty(u_phone)){
            Toast.makeText(this, "Please enter phone number", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(u_password)){
            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
        }else{
            loading.setTitle("Login....");
            loading.setMessage("please wait fetching your data");
            loading.setCanceledOnTouchOutside(false);
            loading.show();
            AllowAccessToAccount(u_phone, u_password);
        }
    }

    private void AllowAccessToAccount(final String phone,final String Password) {

            // Paper is used for automatically login
            Paper.book().write(prevalent.userPhoneKey, phone);
            Paper.book().write(prevalent.userPassword, Password);

            //database reference
            final DatabaseReference rootRef;
            rootRef = FirebaseDatabase.getInstance().getReference();

            if(phone.equals("1122334455")){
                rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.child("Admin").exists()) {
                            //User userData = snapshot.child("Admin").child(phone).getValue(User.class);
                            String s = snapshot.child("Admin").child("Password").getValue().toString();
                            if (s.equals(Password)) {
                                Toast.makeText(LoginPage.this, "Logged in successfully", Toast.LENGTH_SHORT).show();
                                loading.dismiss();
                                //prevalent.CurrentOnlineUser = userData;
                                Intent i = new Intent(LoginPage.this, AdminHomeActivity.class);
                                startActivity(i);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
            else{
                rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.child("Users").child(phone).exists()){
                            User userData = snapshot.child("Users").child(phone).getValue(User.class);
                            if(userData.getPhone().equals(phone)){
                                if(userData.getPassword().equals(Password)){
                                    Toast.makeText(LoginPage.this, "Logged in successfully", Toast.LENGTH_SHORT).show();
                                    loading.dismiss();
                                    new prevalent(userData);
                                    Intent i = new Intent(LoginPage.this, HomeActivity.class);
                                    startActivity(i);
                                }else{
                                    Toast.makeText(LoginPage.this, "Password wrong", Toast.LENGTH_SHORT).show();
                                    loading.dismiss();
                                }
                            }else{
                                Toast.makeText(LoginPage.this, "Please Register your number!", Toast.LENGTH_SHORT).show();
                                loading.dismiss();
                            }
                        }else{

                            Toast.makeText(LoginPage.this,
                                    "Account with this " + phone + " number is not exists.",
                                    Toast.LENGTH_SHORT).show();

                            loading.dismiss();
                            Toast.makeText(LoginPage.this, "Create new account!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

    }
}