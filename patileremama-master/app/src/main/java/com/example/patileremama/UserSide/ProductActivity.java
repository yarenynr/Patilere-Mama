package com.example.patileremama.UserSide;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.patileremama.R;
import com.example.patileremama.authorized.AuthProductDetail;
import com.example.patileremama.authorized.AuthorizedProduct;
import com.example.patileremama.authorized.PaymentsActivity;
import com.example.patileremama.models.Product;
import com.example.patileremama.recylerAdapters.ProductAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ProductActivity extends AppCompatActivity {
    ArrayList<Product> productList;
    Product product;
    ProductAdapter productAdapter;
    RecyclerView recyclerView;
    private FirebaseDatabase db;// Database e ulaşmamızı sağlayan sınıftan nesne oluşturduk
    private DatabaseReference dbRef; // database
    Bundle bundle;
    String category = "";
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        bundle = getIntent().getExtras();
        mAuth = FirebaseAuth.getInstance();
        category = bundle.getString("category");
        db = FirebaseDatabase.getInstance();// veritabanına erişim sağladık
        recyclerView=findViewById(R.id.rv_products);
        productList= new ArrayList<>();
        getValues();
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        //listenin elemanlarının click fonksiyonları

        recyclerView.addOnItemTouchListener(new CategoryActivity.RecyclerItemClickListener(getApplicationContext(), recyclerView, new CategoryActivity.RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                product=productAdapter.getItemName(position);
                Intent intent=new Intent(ProductActivity.this, ProductDetailActivity.class);
                intent.putExtra("category",category);
                intent.putExtra("image",product.image);
                intent.putExtra("kilo",product.kilo);
                intent.putExtra("cost",product.cost);
                intent.putExtra("markName",product.markName);
                startActivity(intent);
            }

            @Override
            public void onLongItemClick(View view, int position) {


            }
        }));


    }
    private void getValues() {
        dbRef = db.getReference("Products").child(category);
        dbRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                productList.clear();
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    Log.d("snapshot= ",ds.toString());
                    Product task=ds.getValue(Product.class);
                    productList.add(task);
                    Log.d("VALUE",ds.getKey().toString());
                }


                productAdapter =new ProductAdapter(getApplicationContext(),productList);
                productAdapter.notifyDataSetChanged();
                recyclerView.setAdapter(productAdapter);


                Log.d(TAG, "onDataChange: selam"+dataSnapshot.getValue()+"sea"+dataSnapshot.getChildren());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_user, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.informations:
                Intent intent = new Intent(ProductActivity.this, ProfileActivity.class);
                startActivity(intent);
                break;

            case R.id.mybox:
                intent = new Intent(ProductActivity.this, MyBoxActivity.class);
                startActivity(intent);
                break;

            case R.id.logout:
                intent = new Intent(ProductActivity.this, UserLoginActivity.class);
                startActivity(intent);
                mAuth.signOut();
                finish();
                break;
        }
        return true;
    }
}