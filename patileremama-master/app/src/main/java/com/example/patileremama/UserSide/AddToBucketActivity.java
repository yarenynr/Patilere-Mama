package com.example.patileremama.UserSide;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.patileremama.R;
import com.example.patileremama.models.Bag;
import com.example.patileremama.recylerAdapters.BagAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class AddToBucketActivity extends AppCompatActivity {
    ArrayList<Bag> bucketArrayList;
    TextView tv_username;
    Bag bag;
    private FirebaseAuth mAuth;
    BagAdapter bagAdapter;
    RecyclerView recyclerView;
    private FirebaseDatabase db,dbBucket;// Database e ulaşmamızı sağlayan sınıftan nesne oluşturduk
    private DatabaseReference dbRef,dbBucketRef; // databasede hangi path e ulaşacağımızı belirlediğimiz sınıftan nesne oluşturduk
    private FirebaseUser fUser; // firebasein kullanıcı sınıfından bir nesne oluşturduk current user değerlerini alabilmek için
    String selectedIndex;
    Bundle bundle;
    String userId,username,address,email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_bucket);
        db = FirebaseDatabase.getInstance();// veritabanına erişim sağladık
        dbBucket = FirebaseDatabase.getInstance();// veritabanına erişim sağladık
        fUser = FirebaseAuth.getInstance().getCurrentUser(); // şuanki kullanıcının verilerini firebaseuser classının nesnesinin içine attık
        mAuth = FirebaseAuth.getInstance();
        recyclerView=findViewById(R.id.rv_addtobucket);
        tv_username=findViewById(R.id.tv_username);
        bucketArrayList = new ArrayList<>();
        bundle= getIntent().getExtras();
        userId=bundle.getString("userId");//bundle ve getextras sayesinde önceki sayfadan buraya gönderilen bilgileri çekebiliyoruz
        username=bundle.getString("username");
        address=bundle.getString("address");
        email=bundle.getString("email");
        tv_username.setText(username);
        StoreDataList();
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));


        //listenin elemanlarının click fonksiyonları

        recyclerView.addOnItemTouchListener(new CategoryActivity.RecyclerItemClickListener(getApplicationContext(), recyclerView, new CategoryActivity.RecyclerItemClickListener.OnItemClickListener()  {
            @Override
            public void onItemClick(View view, int position) {
                selectedIndex=String.valueOf(position);
                bag= bagAdapter.getItemName(position);
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(AddToBucketActivity.this);
                final EditText edittext = new EditText(AddToBucketActivity.this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                edittext.setLayoutParams(lp);
                alertDialog.setView(edittext);
                alertDialog.setMessage("Lütfen Adet Giriniz");
                alertDialog.setTitle("Sepete Ekle");


                alertDialog.setPositiveButton("Ekle", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        int YourEditTextValue = Integer.parseInt(edittext.getText().toString());
                        if (YourEditTextValue > 0 && YourEditTextValue <= bag.count) {
                            dbBucketRef = dbBucket.getReference("Buckets/" + fUser.getUid()+"/"+bag.markName);
                            HashMap<String, Object> hashMap = new HashMap<>();
                            hashMap.put("id", fUser.getUid());
                            hashMap.put("boxId", userId);
                            hashMap.put("boxName", username);
                            hashMap.put("boxMarkName", bag.markName);
                            hashMap.put("address", address);
                            hashMap.put("image", bag.image);
                            hashMap.put("cost", bag.cost);
                            hashMap.put("kilo", bag.kilo);
                            hashMap.put("count", YourEditTextValue);
                            dbBucketRef.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getApplicationContext(), "Ürün sepete eklendi", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }else{
                            Toast.makeText(getApplicationContext(), "Lütfen geçerli bir adet giriniz", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                alertDialog.setNegativeButton("İptal", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                });

                alertDialog.show();

            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));



    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_orherbox, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.informations:
                Intent intent = new Intent(AddToBucketActivity.this, ProfileActivity.class);
                startActivity(intent);
                break;

            case R.id.mybucket:
                intent = new Intent(AddToBucketActivity.this, BucketActivity.class);
                startActivity(intent);
                break;

            case R.id.logout:
                intent = new Intent(AddToBucketActivity.this, UserLoginActivity.class);
                startActivity(intent);
                mAuth.signOut();
                finish();
                break;
        }
        return true;
    }

    //Ürünleri çağırdığımız yer
    private void StoreDataList() {
        dbRef = db.getReference("Bags/"+userId);
        dbRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                bucketArrayList.clear();
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    Log.d("snapshot= ",ds.toString());
                    Bag task=ds.getValue(Bag.class);
                    bucketArrayList.add(task);
                    Log.d("VALUE",ds.getKey().toString());
                }

                bagAdapter =new BagAdapter(getApplicationContext(), bucketArrayList,false);
                bagAdapter.notifyDataSetChanged();
                recyclerView.setAdapter(bagAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}
