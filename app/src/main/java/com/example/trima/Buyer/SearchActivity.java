package com.example.trima.Buyer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.trima.R;
import com.example.trima.ViewHolder.ProductViewHolder;
import com.example.trima.model.Product;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class SearchActivity extends AppCompatActivity {

    private Button searchButton;
    private EditText inputText;
    private RecyclerView searchList;
    private String searchInput;
    private DatabaseReference productref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchButton = (Button)findViewById(R.id.search_button);
        inputText = (EditText)findViewById(R.id.search_product_name);
        searchList = (RecyclerView)findViewById(R.id.search_list);

        //inputText.setAutofillHints(View.AUTOFILL_HINT_NAME);

        searchList.setLayoutManager(new LinearLayoutManager(SearchActivity.this));

        productref = FirebaseDatabase.getInstance().getReference().child("Products");

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchInput = inputText.getText().toString();
                onStart();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<Product> options = new FirebaseRecyclerOptions.Builder<Product>()
                .setQuery(productref.orderByChild("ProductName").startAt(searchInput).endAt(searchInput), Product.class)
                .build();
        FirebaseRecyclerAdapter<Product, ProductViewHolder> adapter = new FirebaseRecyclerAdapter<Product, ProductViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull final Product model) {

                holder.productName.setText(model.getProductName());
                holder.productPrice.setText("Price : " + model.getPrice() + "$");
                holder.productDescription.setText(model.getDescription());
                Picasso.get().load(model.getImage()).into(holder.productImage);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(SearchActivity.this, ProductDetailsActivity.class);
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
        searchList.setAdapter(adapter);
        adapter.startListening();
    }
}