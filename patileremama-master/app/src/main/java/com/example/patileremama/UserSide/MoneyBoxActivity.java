package com.example.patileremama.UserSide;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.example.patileremama.R;
import com.google.firebase.auth.FirebaseAuth;

public class MoneyBoxActivity extends AppCompatActivity {
    private FirebaseAuth fAuth;
    ImageButton ib_create,ib_support;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money_box);
        fAuth = FirebaseAuth.getInstance();
        ib_create=findViewById(R.id.ib_create);
        ib_support=findViewById(R.id.ib_support);

        ib_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MoneyBoxActivity.this,CategoryActivity.class);
                startActivity(intent);
            }
        });

        ib_support.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MoneyBoxActivity.this,OtherBoxesActivity.class);
                startActivity(intent);
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
               Intent intent = new Intent(MoneyBoxActivity.this, ProfileActivity.class);
                startActivity(intent);
                break;

            case R.id.mybox:
               intent = new Intent(MoneyBoxActivity.this, MyBoxActivity.class);
                startActivity(intent);
                break;

            case R.id.logout:
                 intent = new Intent(MoneyBoxActivity.this, UserLoginActivity.class);
                startActivity(intent);
                fAuth.signOut();
                finish();
                break;
        }
        return true;
    }
}