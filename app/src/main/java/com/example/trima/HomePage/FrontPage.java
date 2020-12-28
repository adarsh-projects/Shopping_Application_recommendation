package com.example.trima.HomePage;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.trima.Buyer.LoginPage;
import com.example.trima.Buyer.RegisterPage;
import com.example.trima.R;
import com.example.trima.Seller.SellerLoginActivity;

import io.paperdb.Paper;

public class FrontPage extends AppCompatActivity {
    private Button Join, Amember;
    private TextView sellerloginpage;
    private ProgressDialog loading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_front_page);

        Join = (Button) findViewById(R.id.join);
        Amember = (Button) findViewById(R.id.member);
        sellerloginpage = (TextView)findViewById(R.id.seller_login_page);

        sellerloginpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(FrontPage.this, SellerLoginActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                finish();
            }
        });

        Paper.init(this);
        loading = new ProgressDialog(this);

        Join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(FrontPage.this, RegisterPage.class);
                startActivity(i);
            }
        });
        Amember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(FrontPage.this, LoginPage.class);
                startActivity(i);
            }
        });
    }
}