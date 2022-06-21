package com.example.patileremama.UserSide;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.patileremama.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;

public class UserRegisterActivity extends AppCompatActivity {

    FirebaseStorage storage;
    StorageReference storageReference;

    private EditText registerPassword;
    private EditText registerEmail;
    private EditText registerUserName;
    private EditText registerAddress;
    private Button buttonRegister;
    private FirebaseAuth mAuth;
    private DatabaseReference reference;

    private String userPassword;
    private String userEmail;
    private String userName;
    private String userAddress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);
        registerPassword = (EditText)findViewById(R.id.registerPassword);
        registerEmail = (EditText)findViewById(R.id.registerUserEmail);
        registerUserName = (EditText)findViewById(R.id.registerUserName);
        registerAddress = (EditText)findViewById(R.id.registerAddress);
        buttonRegister = (Button) findViewById(R.id.buttonRegister);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        mAuth = FirebaseAuth.getInstance();


        // register buton tiklaninca
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Tüm kullanıcı bilgilerini alıyoruz eğer boş değillerse veriabanına gönderiyoruz kullanıcı kaydı olmuş oluyor

                userPassword = registerPassword.getText().toString();
                userEmail = registerEmail.getText().toString();
                userAddress = registerAddress.getText().toString();
                userName = registerUserName.getText().toString();
                if(userPassword.isEmpty() || userEmail.isEmpty() || userAddress.isEmpty() || userName.isEmpty()){

                    Toast.makeText(getApplicationContext(),"Lütfen gerekli alanları doldurunuz!",Toast.LENGTH_SHORT).show();

                }else{
                    register();

                }

            }
        });
    }
    private void register() {
        //Girilen bilgileri veritabanına gönderdiğimiz kısım

                        mAuth.createUserWithEmailAndPassword(userEmail,userPassword)
                                .addOnCompleteListener(UserRegisterActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(Task<AuthResult> task) {
                                        if(task.isSuccessful()){

                                            FirebaseUser firebaseUser=mAuth.getCurrentUser();
                                            String userid=firebaseUser.getUid();

                                            reference= FirebaseDatabase.getInstance().getReference("Users").child(userid);

                                            HashMap<String, String> hashMap= new HashMap<>();
                                            hashMap.put("id",userid);
                                            hashMap.put("address",userAddress);
                                            hashMap.put("email", userEmail);
                                            hashMap.put("username", userName);
                                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                                    .setDisplayName(userName)
                                                    .build();
                                            firebaseUser.updateProfile(profileUpdates)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {

                                                            }
                                                        }
                                                    });

                                            reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()){
                                                        Intent i = new Intent(UserRegisterActivity.this, UserLoginActivity.class);
                                                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                        startActivity(i);
                                                        finish();
                                                    }
                                                }
                                            });



                                        }
                                        else{
                                            Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                                        }

                                    }


        });
    }


    }



