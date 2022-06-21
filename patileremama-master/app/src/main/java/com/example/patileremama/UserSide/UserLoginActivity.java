package com.example.patileremama.UserSide;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.patileremama.R;
import com.example.patileremama.authorized.AuthorizedCategory;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserLoginActivity extends AppCompatActivity {

    private EditText editTextUserName;
    private EditText editTextUserPassword;
    private Button buttonLogin;
    private TextView txtRegister;
    private TextView txtReset;
    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;
    private String userName;
    private String userPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);
        editTextUserName = (EditText)findViewById(R.id.editTextUserName);
        editTextUserPassword = (EditText)findViewById(R.id.editTextUserPassword);
        buttonLogin = (Button) findViewById(R.id.buttonLogin);
        txtRegister = (TextView) findViewById(R.id.txtRegister);

        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser(); // authenticate olan kullaniciyi aliyoruz eger var ise

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                userName = editTextUserName.getText().toString();//yazdığımız login emailini çekiyoruz gettext ile
                userPassword = editTextUserPassword.getText().toString();// yazdığımız passwordu çekiyoruz gettext ile
                if(userName.isEmpty() || userPassword.isEmpty()){// is empty komutuyla verilerin boş olup olmadığını kontrol ediyoruz

                    Toast.makeText(getApplicationContext(),"Lütfen gerekli alanları doldurunuz!",Toast.LENGTH_SHORT).show();

                }else{
                        login();

                }
            }
        });

        txtRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserLoginActivity.this, UserRegisterActivity.class);
                startActivity(intent);
            }
        });
    }
    private void login() {
        // mAuth nesnemiz sign in with email paswword ile böyle bir kullanıcı var mı yok mu kontrol ediyor hazır bir koddur
        mAuth.signInWithEmailAndPassword(userName,userPassword).addOnCompleteListener(UserLoginActivity.this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){// dönen sonuç basarılı ise
                            if(userName.equals("admin@gmail.com")){
                                Intent i = new Intent(UserLoginActivity.this, AuthorizedCategory.class);
                                startActivity(i);
                                finish();
                            }else{
                                Intent i = new Intent(UserLoginActivity.this, MoneyBoxActivity.class);
                                startActivity(i);
                                finish();
                            }
                            //ChoosePage sayfamıza yönlendirme yapıyoruz


                        }
                        else{
                            // hata mesajını ekrana veren kod
                            Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }

                });
    }
}