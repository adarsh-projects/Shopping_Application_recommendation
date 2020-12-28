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

public class SellerLoginActivity extends AppCompatActivity {

    private EditText sellerEmail, sellerPassword;
    private Button login;
    private TextView registerPage;
    private ProgressDialog loadingBar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_login);

        sellerEmail = (EditText)findViewById(R.id.seller_login_email);
        sellerPassword = (EditText)findViewById(R.id.seller_login_password);
        registerPage = (TextView)findViewById(R.id.seller_login_register);
        login = (Button)findViewById(R.id.seller_login_confirm);

        loadingBar = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();
            }
        });

        registerPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SellerLoginActivity.this, SellerRegistrationActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                finish();
            }
        });
    }

    private void loginUser() {
        String email = sellerEmail.getText().toString();
        String password = sellerPassword.getText().toString();
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "please write your Email ", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "please write Password ", Toast.LENGTH_SHORT).show();
        }else{
            loadingBar.setTitle("Login Account...");
            loadingBar.setMessage("Please wait, checking your credentials ");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                loadingBar.dismiss();
                                Intent i = new Intent(SellerLoginActivity.this, SellerHomeActivity.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(i);

                            }else{
                                Toast.makeText(SellerLoginActivity.this, "Please Register before login", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                            }
                        }
                    });
        }
    }
}