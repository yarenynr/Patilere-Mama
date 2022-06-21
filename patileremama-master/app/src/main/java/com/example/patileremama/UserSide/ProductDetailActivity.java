package com.example.patileremama.UserSide;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.example.patileremama.R;
import com.example.patileremama.authorized.AuthProductDetail;
import com.example.patileremama.authorized.AuthorizedProduct;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class ProductDetailActivity extends AppCompatActivity {
    private DatabaseReference reference;
    private FirebaseAuth mAuth;
    TextView et_productname, et_productkilo, et_productcost;
    Button btn_productAdd;
    Bundle bundle;
    String image="";
    String name = "";
    int kilo = 0;
    int cost = 0;
    ImageView iv_image;
    String category = "";
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
            bundle = getIntent().getExtras();
            category=bundle.getString("category");
            name = bundle.getString("markName");
            kilo = bundle.getInt("kilo");
            cost = bundle.getInt("cost");
            image=bundle.getString("image");

        et_productname = findViewById(R.id.tv_productname);
        et_productkilo = findViewById(R.id.tv_productkilo);
        et_productcost = findViewById(R.id.tv_productcost);
        btn_productAdd = findViewById(R.id.button);
        iv_image = findViewById(R.id.iv_image);
        et_productname.setText(name);
        et_productkilo.setText(String.valueOf(kilo));
        et_productcost.setText(String.valueOf(cost));
        Glide.with(ProductDetailActivity.this).load(image).into(iv_image);

        // get the Firebase  storage reference
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();


        btn_productAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!et_productname.getText().toString().isEmpty() && !et_productkilo.getText().toString().isEmpty() && !et_productcost.getText().toString().isEmpty()) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(ProductDetailActivity.this);
                    final EditText edittext = new EditText(ProductDetailActivity.this);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT);
                    edittext.setLayoutParams(lp);
                    alertDialog.setView(edittext);
                    alertDialog.setMessage("Lütfen Adet Giriniz");
                    alertDialog.setTitle("Kumbaraya Ekle");


                    alertDialog.setPositiveButton("Ekle", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {

                            int YourEditTextValue = Integer.parseInt(edittext.getText().toString());
                            if (YourEditTextValue > 0 ) {
                                    addToBag(YourEditTextValue);
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
                Intent intent = new Intent(ProductDetailActivity.this, ProfileActivity.class);
                startActivity(intent);
                break;

            case R.id.mybox:
                intent = new Intent(ProductDetailActivity.this, MyBoxActivity.class);
                startActivity(intent);
                break;

            case R.id.logout:
                intent = new Intent(ProductDetailActivity.this, UserLoginActivity.class);
                startActivity(intent);
                mAuth.signOut();
                finish();
                break;
        }
        return true;
    }
    void addToBag(int count){
        reference = FirebaseDatabase.getInstance().getReference("Bags").child(firebaseUser.getUid()).child(et_productname.getText().toString());

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("markName", name);
        hashMap.put("kilo", kilo);
        hashMap.put("cost", cost);
        hashMap.put("image", image);
        hashMap.put("count", count);

        reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(ProductDetailActivity.this, "Ürün kumbaraya eklendi", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(ProductDetailActivity.this, MyBoxActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        });
    }
}