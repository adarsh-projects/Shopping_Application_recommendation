package com.example.trima.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.trima.HomePage.FrontPage;
import com.example.trima.R;

import io.paperdb.Paper;

public class AdminHomeActivity extends AppCompatActivity {
    private Button logout1,orderlist, manageProduct;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        logout1 = (Button)findViewById(R.id.logout);
        manageProduct = (Button)findViewById(R.id.checkProduct);

        logout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Paper.book().destroy();
                Intent i = new Intent(AdminHomeActivity.this, FrontPage.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                finish();
            }
        });
        manageProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AdminHomeActivity.this, AdminProductCheckingActivity.class);
                startActivity(i);
                finish();
            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}