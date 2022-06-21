package com.example.patileremama.authorized;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.patileremama.R;
import com.example.patileremama.UserSide.CategoryActivity;
import com.example.patileremama.UserSide.UserLoginActivity;
import com.example.patileremama.models.Product;
import com.example.patileremama.recylerAdapters.ProductAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;


public class AuthorizedProduct extends AppCompatActivity {
    Button btn_prod_add;
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
        bundle = getIntent().getExtras();
        category = bundle.getString("category");
        setContentView(R.layout.activity_authorized_product);
        btn_prod_add=findViewById(R.id.btn_prod_add);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();// veritabanına erişim sağladık
        recyclerView=findViewById(R.id.rv_authproducts);
        productList= new ArrayList<>();
        getValues();
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        //listenin elemanlarının click fonksiyonları

        recyclerView.addOnItemTouchListener(new CategoryActivity.RecyclerItemClickListener(getApplicationContext(), recyclerView, new CategoryActivity.RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                product=productAdapter.getItemName(position);
                Intent intent=new Intent(AuthorizedProduct.this,AuthProductDetail.class);
                Toast.makeText(AuthorizedProduct.this, category, Toast.LENGTH_SHORT).show();
                intent.putExtra("category",category);
                intent.putExtra("image",product.image);
                intent.putExtra("kilo",product.kilo);
                intent.putExtra("cost",product.cost);
                intent.putExtra("markName",product.markName);
                intent.putExtra("isUpdate",true);
                startActivity(intent);
            }

            @Override
            public void onLongItemClick(View view, int position) {
                product=productAdapter.getItemName(position);
                selectIslem();
            }



            private void selectIslem() {
                final CharSequence[] options = {"Sil"};
                AlertDialog.Builder builder = new AlertDialog.Builder(AuthorizedProduct.this);
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (options[item].equals("Sil"))
                        {
                            db.getReference("Products").child(category).child(product.markName).removeValue();
                            Toast.makeText(AuthorizedProduct.this, "Ürün Silindi", Toast.LENGTH_SHORT).show();
                            productAdapter.notifyDataSetChanged();//sayfayı yenilemeye yarar(recylerview i yeniler)
                        }
                    }
                });
                builder.show();
            }
        }));



        btn_prod_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AuthorizedProduct.this,AuthProductDetail.class);
                intent.putExtra("category",category);
                startActivity(intent);
            }
        });

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
        getMenuInflater().inflate(R.menu.menu_auth, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.payments:
                Intent intent = new Intent(AuthorizedProduct.this, PaymentsActivity.class);
                startActivity(intent);
                break;

            case R.id.logout:
                intent = new Intent(AuthorizedProduct.this, UserLoginActivity.class);
                startActivity(intent);
                mAuth.signOut();
                finish();
                break;
        }
        return true;
    }
}