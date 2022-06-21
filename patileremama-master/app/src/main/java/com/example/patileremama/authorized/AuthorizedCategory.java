package com.example.patileremama.authorized;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.patileremama.R;
import com.example.patileremama.UserSide.BucketActivity;
import com.example.patileremama.UserSide.CategoryActivity;
import com.example.patileremama.UserSide.ProductActivity;
import com.example.patileremama.UserSide.UserLoginActivity;
import com.example.patileremama.models.Category;
import com.example.patileremama.recylerAdapters.CategoryAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class AuthorizedCategory extends AppCompatActivity {
    Button btn_cat_add;
    ArrayList<Category> categoryList;
    Category category;
    CategoryAdapter categoryAdapter;
    RecyclerView recyclerView;
    private FirebaseDatabase db;// Database e ulaşmamızı sağlayan sınıftan nesne oluşturduk
    private DatabaseReference dbRef; // database
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorized_category);
        btn_cat_add=findViewById(R.id.btn_cat_add);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();// veritabanına erişim sağladık
        recyclerView=findViewById(R.id.rv_authcategory);
        categoryList= new ArrayList<>();
        getValues();
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        //listenin elemanlarının click fonksiyonları

        recyclerView.addOnItemTouchListener(new CategoryActivity.RecyclerItemClickListener(getApplicationContext(), recyclerView, new CategoryActivity.RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                category=categoryAdapter.getItemName(position);
                Intent intent=new Intent(AuthorizedCategory.this,AuthorizedProduct.class);
                intent.putExtra("category",category.name);
                Toast.makeText(AuthorizedCategory.this, category.name, Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }

            @Override
            public void onLongItemClick(View view, int position) {
                category=categoryAdapter.getItemName(position);
                selectIslem();
            }
        }));

        btn_cat_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(AuthorizedCategory.this);
                final EditText edittext = new EditText(AuthorizedCategory.this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                edittext.setLayoutParams(lp);
                alertDialog.setView(edittext);
                alertDialog.setMessage("Yeni Kategori");
                alertDialog.setTitle("Kategori Ekle");


                alertDialog.setPositiveButton("Ekle", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        String category = edittext.getText().toString();
                        if(category.trim().length()>1){
                            dbRef = db.getReference("Category").child(category);
                            HashMap<String, Object> hashMap = new HashMap<>();
                            hashMap.put("name", category);
                            dbRef.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getApplicationContext(), "Kategori Eklendi!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }else{
                            Toast.makeText(AuthorizedCategory.this, "Geçerli bir kategori giriniz", Toast.LENGTH_SHORT).show();
                        }


                    }
                });

                alertDialog.setNegativeButton("İptal", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                });

                alertDialog.show();


            }
        });

    }
    private void selectIslem() {
        final CharSequence[] options = {"Sil"};
        AlertDialog.Builder builder = new AlertDialog.Builder(AuthorizedCategory.this);
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Sil"))
                {
                    db.getReference("Category").child(category.name).removeValue();
                    Toast.makeText(AuthorizedCategory.this, "Kategori Silindi", Toast.LENGTH_SHORT).show();
                    categoryAdapter.notifyDataSetChanged();//sayfayı yenilemeye yarar(recylerview i yeniler)
                }
            }
        });
        builder.show();
    }
    private void getValues() {
        dbRef = db.getReference("Category");
        dbRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                categoryList.clear();
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    Log.d("snapshot= ",ds.toString());
                    Category task=ds.getValue(Category.class);
                    categoryList.add(task);
                    Log.d("VALUE",ds.getKey().toString());
                }


                categoryAdapter =new CategoryAdapter(getApplicationContext(),categoryList);
                categoryAdapter.notifyDataSetChanged();
                recyclerView.setAdapter(categoryAdapter);


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
                Intent intent = new Intent(AuthorizedCategory.this, PaymentsActivity.class);
                startActivity(intent);
                break;

            case R.id.logout:
                intent = new Intent(AuthorizedCategory.this, UserLoginActivity.class);
                startActivity(intent);
                mAuth.signOut();
                finish();
                break;
        }
        return true;
    }
}