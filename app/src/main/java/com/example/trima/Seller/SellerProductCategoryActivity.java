package com.example.trima.Seller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.trima.R;

import io.paperdb.Paper;

public class SellerProductCategoryActivity extends AppCompatActivity {
    private ImageView tshirt, sports, female_dress, swether;
    private ImageView glasses, purses_bag, hats, shoes;
    private ImageView headphone, mobile, watches, laptop;
    private ImageView shirt, pant, girl_shorts, men_shorts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_product_category);

        Paper.init(this);

        tshirt = (ImageView)findViewById(R.id.admin_tshirt);
        sports = (ImageView)findViewById(R.id.admin_sports);
        female_dress = (ImageView)findViewById(R.id.admin_female_dress);
        swether = (ImageView)findViewById(R.id.admin_swether);

        glasses = (ImageView)findViewById(R.id.admin_glasses);
        purses_bag = (ImageView)findViewById(R.id.admin_purses_bags);
        hats = (ImageView)findViewById(R.id.admin_hats);
        shoes = (ImageView)findViewById(R.id.admin_shoess);

        headphone = (ImageView)findViewById(R.id.admin_headphoness);
        laptop = (ImageView)findViewById(R.id.admin_laptop);
        watches = (ImageView)findViewById(R.id.admin_watches);
        mobile = (ImageView)findViewById(R.id.admin_mobiles);


        shirt = (ImageView)findViewById(R.id.admin_shirt);
        pant = (ImageView) findViewById(R.id.admin_pants);
        girl_shorts = (ImageView)findViewById(R.id.admin_shorts_girl);
        men_shorts = (ImageView)findViewById(R.id.admin_shorts_men);

        shirt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SellerProductCategoryActivity.this, SellerAddNewProductActivity.class);
                i.putExtra("Category", "Shirt");
                startActivity(i);
            }
        });
        pant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SellerProductCategoryActivity.this, SellerAddNewProductActivity.class);
                i.putExtra("Category", "Pant");
                startActivity(i);
            }
        });
        girl_shorts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SellerProductCategoryActivity.this, SellerAddNewProductActivity.class);
                i.putExtra("Category", "Girl_Shorts");
                startActivity(i);
            }
        });
        men_shorts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SellerProductCategoryActivity.this, SellerAddNewProductActivity.class);
                i.putExtra("Category", "Men_Shorts");
                startActivity(i);
            }
        });

        tshirt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SellerProductCategoryActivity.this, SellerAddNewProductActivity.class);
                i.putExtra("Category", "T-Shirts");
                startActivity(i);
            }
        });
        sports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SellerProductCategoryActivity.this, SellerAddNewProductActivity.class);
                i.putExtra("Category", "Sports_TShirt");
                startActivity(i);
            }
        });
        female_dress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SellerProductCategoryActivity.this, SellerAddNewProductActivity.class);
                i.putExtra("Category", "FemaleDress");
                startActivity(i);
            }
        });
        swether.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SellerProductCategoryActivity.this, SellerAddNewProductActivity.class);
                i.putExtra("Category", "Sweater");
                startActivity(i);
            }
        });

        glasses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SellerProductCategoryActivity.this, SellerAddNewProductActivity.class);
                i.putExtra("Category", "Glasses");
                startActivity(i);
            }
        });
        purses_bag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SellerProductCategoryActivity.this, SellerAddNewProductActivity.class);
                i.putExtra("Category", "PursBag");
                startActivity(i);
            }
        });
        hats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SellerProductCategoryActivity.this, SellerAddNewProductActivity.class);
                i.putExtra("Category", "Hats");
                startActivity(i);
            }
        });
        shoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SellerProductCategoryActivity.this, SellerAddNewProductActivity.class);
                i.putExtra("Category", "Shoes");
                startActivity(i);
            }
        });

        headphone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SellerProductCategoryActivity.this, SellerAddNewProductActivity.class);
                i.putExtra("Category", "HeadPhone");
                startActivity(i);
            }
        });
        laptop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SellerProductCategoryActivity.this, SellerAddNewProductActivity.class);
                i.putExtra("Category", "Laptop");
                startActivity(i);
            }
        });
        mobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SellerProductCategoryActivity.this, SellerAddNewProductActivity.class);
                i.putExtra("Category", "Mobile");
                startActivity(i);
            }
        });
        watches.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SellerProductCategoryActivity.this, SellerAddNewProductActivity.class);
                i.putExtra("Category", "Watches");
                startActivity(i);
            }
        });


    }

}