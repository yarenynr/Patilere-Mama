package com.example.patileremama.UserSide;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.patileremama.R;
import com.example.patileremama.models.Category;
import com.example.patileremama.models.CurrentUser;
import com.example.patileremama.recylerAdapters.CategoryAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class ProfileActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    FirebaseUser firebaseUser;
    private FirebaseDatabase db;// Database e ulaşmamızı sağlayan sınıftan nesne oluşturduk
    private DatabaseReference dbRef; // databasede hangi path e ulaşacağımızı belirlediğimiz sınıftan nesne oluşturduk
    CurrentUser cuser;
    EditText et_fullname,et_address;
    TextView username;
    Button btn_update;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        db = FirebaseDatabase.getInstance();// veritabanına erişim sağladık
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();
        btn_update=findViewById(R.id.btn_update);
        et_fullname=findViewById(R.id.et_fullname);
        et_address=findViewById(R.id.et_address);
        username=findViewById(R.id.username);
        getValues();
        username.setText(firebaseUser.getDisplayName());

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!et_address.getText().toString().trim().isEmpty()&&!et_fullname.getText().toString().trim().isEmpty()){
                    dbRef= FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());

                    HashMap<String, Object> hashMap= new HashMap<>();
                    hashMap.put("address",et_address.getText().toString().trim());
                    hashMap.put("username", et_fullname.getText().toString().trim());
                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(et_fullname.getText().toString().trim())
                            .build();
                    firebaseUser.updateProfile(profileUpdates)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {

                                    }
                                }
                            });

                    dbRef.updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(ProfileActivity.this, "Profile Güncellendi", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else{
                    Toast.makeText(ProfileActivity.this, "Tüm verileri gönder", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void getValues() {
        dbRef = db.getReference("Users").child(firebaseUser.getUid());
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    CurrentUser task=dataSnapshot.getValue(CurrentUser.class);
                    cuser=task;
                Log.d(TAG, "onDataChange: "+dataSnapshot.getValue());
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}