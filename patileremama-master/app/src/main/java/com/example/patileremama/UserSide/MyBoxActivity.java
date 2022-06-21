package com.example.patileremama.UserSide;



import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import android.widget.Toast;

import com.example.patileremama.R;
import com.example.patileremama.models.Bag;
import com.example.patileremama.recylerAdapters.BagAdapter;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;


public class MyBoxActivity extends AppCompatActivity {

    ArrayList<Bag> bucketArrayList;
    Bag bag;
    BagAdapter bagAdapter;
    RecyclerView recyclerView;
    private FirebaseDatabase db,dbBucket;// Database e ulaşmamızı sağlayan sınıftan nesne oluşturduk
    private DatabaseReference dbRef,dbBucketRef; // databasede hangi path e ulaşacağımızı belirlediğimiz sınıftan nesne oluşturduk
    private FirebaseUser fUser; // firebasein kullanıcı sınıfından bir nesne oluşturduk current user değerlerini alabilmek için
    String selectedIndex;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_box); db = FirebaseDatabase.getInstance();// veritabanına erişim sağladık
        dbBucket = FirebaseDatabase.getInstance();// veritabanına erişim sağladık
        fUser = FirebaseAuth.getInstance().getCurrentUser(); // şuanki kullanıcının verilerini firebaseuser classının nesnesinin içine attık
        recyclerView=findViewById(R.id.rv_bags);
        bucketArrayList = new ArrayList<>();

        StoreDataList();
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));


        //listenin elemanlarının click fonksiyonları

        recyclerView.addOnItemTouchListener(new CategoryActivity.RecyclerItemClickListener(getApplicationContext(), recyclerView, new CategoryActivity.RecyclerItemClickListener.OnItemClickListener()  {
            @Override
            public void onItemClick(View view, int position) {

            }

            @Override
            public void onLongItemClick(View view, int position) {
                selectedIndex=String.valueOf(position);
                bag= bagAdapter.getItemName(position);
                selectIslem();
            }
        }));



    }

    //Ürünleri çağırdığımız yer
    private void StoreDataList() {
        dbRef = db.getReference("Bags/"+fUser.getUid());
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

                bagAdapter =new BagAdapter(getApplicationContext(), bucketArrayList,true);
                bagAdapter.notifyDataSetChanged();
                recyclerView.setAdapter(bagAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void selectIslem() {
        final CharSequence[] options = { "Sil"};
        AlertDialog.Builder builder = new AlertDialog.Builder(MyBoxActivity.this);
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Sil"))
                {
                    db.getReference("Bags").child(fUser.getUid()).child(bag.markName).removeValue();
                    Toast.makeText(MyBoxActivity.this, "Mama Silindi", Toast.LENGTH_SHORT).show();

                    bagAdapter.notifyDataSetChanged();//sayfayı yenilemeye yarar(recylerview i yeniler)
                }

            }
        });
        builder.show();
    }
}