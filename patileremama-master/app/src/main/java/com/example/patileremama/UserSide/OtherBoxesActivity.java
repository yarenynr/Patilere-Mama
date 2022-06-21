package com.example.patileremama.UserSide;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.example.patileremama.R;
import com.example.patileremama.models.CurrentUser;
import com.example.patileremama.recylerAdapters.OtherBagAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class OtherBoxesActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<CurrentUser> currentUserArrayList;
    CurrentUser user;
    OtherBagAdapter otherBagAdapter;
    private FirebaseDatabase db;// Database e ulaşmamızı sağlayan sınıftan nesne oluşturduk
    private DatabaseReference dbRef; // databasede hangi path e ulaşacağımızı belirlediğimiz sınıftan nesne oluşturduk
    private FirebaseUser fUser; // firebasein kullanıcı sınıfından bir nesne oluşturduk current user değerlerini alabilmek için
    String selectedIndex;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_boxes);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();// veritabanına erişim sağladık
        fUser = FirebaseAuth.getInstance().getCurrentUser(); // şuanki kullanıcının verilerini firebaseuser classının nesnesinin içine attık
        recyclerView = findViewById(R.id.rv_users);
        currentUserArrayList = new ArrayList<>();
        StoreDataList();
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        //listenin elemanlarının click fonksiyonları

        recyclerView.addOnItemTouchListener(new CategoryActivity.RecyclerItemClickListener(getApplicationContext(), recyclerView, new CategoryActivity.RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                user = otherBagAdapter.getItemName(position);
                Intent intent =new Intent(OtherBoxesActivity.this,AddToBucketActivity.class);
                intent.putExtra("userId",user.id);
                intent.putExtra("address",user.address);
                intent.putExtra("email",user.email);
                intent.putExtra("username",user.username);

                startActivity(intent);
            }

            @Override
            public void onLongItemClick(View view, int position) {
                selectedIndex = String.valueOf(position);

            }
        }));
    }

    private void StoreDataList() {
        dbRef = db.getReference("Users");
        dbRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                currentUserArrayList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    CurrentUser task = ds.getValue(CurrentUser.class);
                    if(task.id.equals(fUser.getUid())){

                    }else{
                        currentUserArrayList.add(task);
                    }

                }

                otherBagAdapter = new OtherBagAdapter(getApplicationContext(), currentUserArrayList);
                otherBagAdapter.notifyDataSetChanged();
                recyclerView.setAdapter(otherBagAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

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
                Intent intent = new Intent(OtherBoxesActivity.this, ProfileActivity.class);
                startActivity(intent);
                break;

            case R.id.mybucket:
                intent = new Intent(OtherBoxesActivity.this, BucketActivity.class);
                startActivity(intent);
                break;

            case R.id.logout:
                intent = new Intent(OtherBoxesActivity.this, UserLoginActivity.class);
                startActivity(intent);
                mAuth.signOut();
                finish();
                break;
        }
        return true;
    }

}