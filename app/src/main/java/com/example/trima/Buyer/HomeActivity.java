package com.example.trima.Buyer;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trima.HomePage.FrontPage;
import com.example.trima.R;
import com.example.trima.ViewHolder.ProductViewHolder;
import com.example.trima.model.Product;
import com.example.trima.prevalent.*;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.tensorflow.lite.Interpreter;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

class Brand extends AppCompatActivity{
    private Context context;
    Brand(Context context){
        this.context = context;
    }
    public String getBrandName(int index){
        String res = "";
        String line = "";
        try {
            BufferedInputStream bufferedInputStream = new BufferedInputStream(context.getResources().openRawResource(R.raw.brand));
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(bufferedInputStream));
            while ((line = reader.readLine()) != null) {
                int id = Integer.parseInt(line.split(",")[0]);
                if(id == index){
                    res = line.split(",")[1];
                    break;
                }
            }
        } catch (IOException e) {
            Toast.makeText(this, "I'm joke to you", Toast.LENGTH_SHORT).show();
        }
        return res;
    }
}
public class HomeActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ProgressDialog loading;
    private CircleImageView profile_image;
    private DatabaseReference productref;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private String type = "", productname = "";
    private Interpreter interpreter;
    private String prdkey = "", prdvalue = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Home");
        setSupportActionBar(toolbar);
        productref = FirebaseDatabase.getInstance().getReference().child("Products");
        profile_image = (CircleImageView)findViewById(R.id.user_profile_image);
        loading = new ProgressDialog(this);

        Paper.init(this);

        FloatingActionButton fab = findViewById(R.id.cart);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(HomeActivity.this, CartActivity.class);
                startActivity(i);
            }
        });

        try {
            interpreter = new Interpreter(loadModelFile(), null);
        } catch (IOException e) {
            e.printStackTrace();
        }


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                int id = menuItem.getItemId();

                if(id == R.id.nav_cart){
                    Intent i = new Intent(HomeActivity.this, CartActivity.class);
                    startActivity(i);
                }else if(id == R.id.nav_search){

                    Intent i = new Intent(HomeActivity.this, SearchActivity.class);
                    startActivity(i);

                }else if(id == R.id.nav_Category){
                    Toast.makeText(HomeActivity.this, "Category", Toast.LENGTH_SHORT).show();
                }else if(id == R.id.nav_setting){
                    Toast.makeText(HomeActivity.this, "Setting", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(HomeActivity.this, SettingActivity.class);
                    startActivity(i);
                }else if(id == R.id.nav_Logout){
                    Paper.book().destroy();
                    Intent i = new Intent(HomeActivity.this, FrontPage.class);
                    startActivity(i);
                    Toast.makeText(HomeActivity.this, "Successfully Logout", Toast.LENGTH_SHORT).show();
                    loading.dismiss();
                }else if(id == R.id.nav_3D_try_on){
                    Toast.makeText(HomeActivity.this, "virtual trial room", Toast.LENGTH_SHORT).show();
                }
                DrawerLayout drawer = (DrawerLayout)findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        View headerView = navigationView.getHeaderView(0);
        TextView username = headerView.findViewById(R.id.user_name);
        CircleImageView circleImageView = headerView.findViewById(R.id.user_profile_image);
        if(type == null){
            username.setText(prevalent.CurrentOnlineUser.getUserName());
            Picasso.get().load(prevalent.CurrentOnlineUser.getImage()).placeholder(R.drawable.profile).into(circleImageView);
        }

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.


        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setDrawerLayout(drawer)
                .build();

        recyclerView = findViewById(R.id.recycler_menu);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }

    private MappedByteBuffer loadModelFile() throws IOException{
        AssetFileDescriptor assetFileDescriptor = this.getAssets().openFd("recommend.tflite");
        FileInputStream fileInputStream = new FileInputStream(assetFileDescriptor.getFileDescriptor());
        FileChannel fileChannel = fileInputStream.getChannel();
        long startOffset = assetFileDescriptor.getStartOffset();
        long length = assetFileDescriptor.getLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, length);
    }

    public float doInference(String val){
        float[] input1 = new float[1];
        input1[0] = Float.parseFloat(val);
        float[][] output = new float[1][2];
        interpreter.run(input1, output);
        return output[0][0];
    }


    @Override
    protected void onStart() {
        super.onStart();
        if(prdvalue.isEmpty()){
            prdkey = "ProductState";
            prdvalue = "Approved";
        }else{
            prdkey = "Category";
        }
        FirebaseRecyclerOptions<Product> options = new FirebaseRecyclerOptions.Builder<Product>()
                .setQuery(productref.orderByChild(prdkey).equalTo(prdvalue), Product.class).build();
        FirebaseRecyclerAdapter<Product, ProductViewHolder> adapter = new FirebaseRecyclerAdapter<Product, ProductViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final ProductViewHolder holder, int position, @NonNull final Product model) {
                    holder.productName.setText(model.getProductName());
                    holder.productPrice.setText("Price : " + model.getPrice());
                    holder.productDescription.setText(model.getDescription());
                    holder.productDescription.setLines(1);
                    holder.pdiscount.setText(model.getProductDiscount());
                    final int num = model.getProductQuantity();
                    if(num > 0){
                        holder.pquantity.setText("In Stock");
                    }else{
                        holder.pquantity.setText("OutOf Stock");
                        holder.pquantity.setTextColor(Color.RED);
                    }
                    Picasso.get().load(model.getImage()).into(holder.productImage);

                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            float f = doInference(String.valueOf(num));
                            Brand b = new Brand(getApplicationContext());
                            int index = (int)f;
                            productname = b.getBrandName(index) ;
                            prdvalue = model.getCategory();
                            Intent i = new Intent(HomeActivity.this, ProductDetailsActivity.class);
                            i.putExtra("pid", model.getPid());
                            startActivity(i);
                        }
                    });
            }

            @NonNull
            @Override
            public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.products_item_layout, parent, false);
                ProductViewHolder holder = new ProductViewHolder(view);
                return holder;
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout)findViewById(R.id.drawer_layout);
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.action_settings){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}