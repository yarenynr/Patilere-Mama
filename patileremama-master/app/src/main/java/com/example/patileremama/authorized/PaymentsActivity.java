package com.example.patileremama.authorized;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.patileremama.R;
import com.example.patileremama.UserSide.CategoryActivity;
import com.example.patileremama.UserSide.ProductActivity;
import com.example.patileremama.UserSide.UserLoginActivity;
import com.example.patileremama.models.Category;
import com.example.patileremama.models.Process;
import com.example.patileremama.recylerAdapters.CategoryAdapter;
import com.example.patileremama.recylerAdapters.ProcessAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PaymentsActivity extends AppCompatActivity {
    ArrayList<Process> processArrayList;
    Process process;
    ProcessAdapter processAdapter;
    RecyclerView recyclerView;
    private FirebaseAuth mAuth;
    private FirebaseDatabase db;// Database e ulaşmamızı sağlayan sınıftan nesne oluşturduk
    private DatabaseReference dbRef; // databasede hangi path e ulaşacağımızı belirlediğimiz sınıftan nesne oluşturduk

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payments);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();// veritabanına erişim sağladık
        recyclerView=findViewById(R.id.rv_payments);
        processArrayList= new ArrayList<>();
        getValues();
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        //listenin elemanlarının click fonksiyonları

        recyclerView.addOnItemTouchListener(new CategoryActivity.RecyclerItemClickListener(getApplicationContext(), recyclerView, new CategoryActivity.RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                process=processAdapter.getItemName(position);

            }

            @Override
            public void onLongItemClick(View view, int position) {


            }
        }));



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
                Intent intent = new Intent(PaymentsActivity.this, PaymentsActivity.class);
                startActivity(intent);
                break;

            case R.id.logout:
                intent = new Intent(PaymentsActivity.this, UserLoginActivity.class);
                startActivity(intent);
                mAuth.signOut();
                finish();
                break;
        }
        return true;
    }


    private void getValues() {
        dbRef = db.getReference("Process");
        dbRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                processArrayList.clear();
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    for(DataSnapshot ds1:ds.getChildren()){
                        Process task=ds1.getValue(Process.class);
                        processArrayList.add(task);
                        Log.d("snapshot= ",ds1.toString());
                        Log.d("array list size"+processArrayList.size(),ds1.toString());
                    }
                }
                processAdapter =new ProcessAdapter(getApplicationContext(),processArrayList);
                processAdapter.notifyDataSetChanged();
                recyclerView.setAdapter(processAdapter);


            }




            @Override
            public void onCancelled(@NonNull DatabaseError error) {


            }
        });



    }
}