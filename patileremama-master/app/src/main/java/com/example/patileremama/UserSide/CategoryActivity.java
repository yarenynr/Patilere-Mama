package com.example.patileremama.UserSide;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.example.patileremama.R;
import com.example.patileremama.authorized.AuthorizedCategory;
import com.example.patileremama.authorized.AuthorizedProduct;
import com.example.patileremama.models.Category;
import com.example.patileremama.recylerAdapters.CategoryAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CategoryActivity extends AppCompatActivity {
    ArrayList<Category> categoryList;
    Category category;
    CategoryAdapter categoryAdapter;
    private FirebaseAuth fAuth;
    RecyclerView recyclerView;
    private FirebaseDatabase db;// Database e ulaşmamızı sağlayan sınıftan nesne oluşturduk
    private DatabaseReference dbRef; // databasede hangi path e ulaşacağımızı belirlediğimiz sınıftan nesne oluşturduk
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        db = FirebaseDatabase.getInstance();// veritabanına erişim sağladık
        recyclerView=findViewById(R.id.tv_category);
        categoryList= new ArrayList<>();
        fAuth = FirebaseAuth.getInstance();
        getValues();
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        //listenin elemanlarının click fonksiyonları

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                category=categoryAdapter.getItemName(position);
                Intent intent=new Intent(CategoryActivity.this, ProductActivity.class);
                intent.putExtra("category",category.name);
                startActivity(intent);
            }

            @Override
            public void onLongItemClick(View view, int position) {


            }
        }));



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
                Log.d("Products",categoryList.get(0).name);

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
        getMenuInflater().inflate(R.menu.menu_user, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.informations:
              Intent intent = new Intent(CategoryActivity.this, ProfileActivity.class);
                startActivity(intent);
                break;

            case R.id.mybox:
                intent = new Intent(CategoryActivity.this, MyBoxActivity.class);
                startActivity(intent);
                break;

            case R.id.logout:
                intent = new Intent(CategoryActivity.this, UserLoginActivity.class);
                startActivity(intent);
                fAuth.signOut();
                finish();
                break;
        }
        return true;
    }
    public static class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener {
        private OnItemClickListener mListener;
        //click için bu kısmı yukarıda recylerview ile kullanıyoruz
        public interface OnItemClickListener {
            public void onItemClick(View view, int position);

            public void onLongItemClick(View view, int position);
        }

        GestureDetector mGestureDetector;

        public RecyclerItemClickListener(Context context, final RecyclerView recyclerView, OnItemClickListener listener) {
            mListener = listener;
            mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && mListener != null) {
                        mListener.onLongItemClick(child, recyclerView.getChildAdapterPosition(child));
                    }
                }
            });
        }

        @Override public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent e) {
            View childView = view.findChildViewUnder(e.getX(), e.getY());
            if (childView != null && mListener != null && mGestureDetector.onTouchEvent(e)) {
                mListener.onItemClick(childView, view.getChildAdapterPosition(childView));
                return true;
            }
            return false;
        }

        @Override public void onTouchEvent(RecyclerView view, MotionEvent motionEvent) { }

        @Override
        public void onRequestDisallowInterceptTouchEvent (boolean disallowIntercept){}
    }
}