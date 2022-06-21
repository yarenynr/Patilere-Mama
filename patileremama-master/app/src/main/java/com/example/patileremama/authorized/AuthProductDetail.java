package com.example.patileremama.authorized;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.patileremama.R;
import com.example.patileremama.UserSide.UserLoginActivity;
import com.example.patileremama.UserSide.UserRegisterActivity;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public class AuthProductDetail extends AppCompatActivity {

    private Uri filePath;

    // request kodu
    private final int PICK_IMAGE_REQUEST = 22;

    FirebaseStorage storage;
    StorageReference storageReference;
    private DatabaseReference reference;
    private FirebaseAuth mAuth;
    UploadTask uploadTask;

    EditText et_productname, et_productkilo, et_productcost;
    ImageView iv_image;
    Button btn_productAdd;


    Bundle bundle;
    String image="";
    String name = "";
    int kilo = 0;
    int cost = 0;
    String category = "";
    boolean isUpdate = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_product_detail);
        bundle = getIntent().getExtras();
        try {
            bundle = getIntent().getExtras();
            category=bundle.getString("category");
            name = bundle.getString("markName");
            kilo = bundle.getInt("kilo");
            cost = bundle.getInt("cost");
            image=bundle.getString("image");
            isUpdate = bundle.getBoolean("isUpdate");
        } catch (Exception e) {
            e.printStackTrace();
        }
        et_productname = findViewById(R.id.et_productname);
        et_productkilo = findViewById(R.id.et_productkilo);
        et_productcost = findViewById(R.id.et_productcost);
        btn_productAdd = findViewById(R.id.btn_product_add);
        iv_image=findViewById(R.id.iv_image);

// firebase referans kodları
        mAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();

        if (isUpdate) {
            et_productname.setText(name);
            et_productkilo.setText(String.valueOf(kilo));
            et_productcost.setText(String.valueOf(cost));
            Glide.with(AuthProductDetail.this).load(image).into(iv_image);
            btn_productAdd.setText("Güncelle");
        }

        btn_productAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!et_productname.getText().toString().isEmpty() && !et_productkilo.getText().toString().isEmpty() && !et_productcost.getText().toString().isEmpty()) {
                    if (isUpdate) {
                        UpdateProduct();
                    } else {
                        uploadImage();
                    }

                } else {
                    Toast.makeText(AuthProductDetail.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
                }
            }
        });

        iv_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectImage();

            }
        });


    }


    private void SelectImage() {


        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(
                        intent,
                        "Select Image from here..."),
                PICK_IMAGE_REQUEST);
    }

    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    Intent data) {

        super.onActivityResult(requestCode,
                resultCode,
                data);

        //  request kodu ve result kodu kontrol etme
        // eğer request kodu  PICK_IMAGE_REQUEST koduna eşitse ve
        // resultCode  RESULT_OK
        // imageview a görseli yerleştir
        if (requestCode == PICK_IMAGE_REQUEST
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null) {

            // verilerin uri sini al
            filePath = data.getData();
            try {

                // Bitmap kullanarak görüntü görünümünde görüntü ayarlama
                Bitmap bitmap = MediaStore
                        .Images
                        .Media
                        .getBitmap(
                                getContentResolver(),
                                filePath);
                iv_image.setImageBitmap(bitmap);
            } catch (IOException e) {

                e.printStackTrace();
            }
        }
    }


    private void uploadImage() {
        if (filePath != null) {

            // upload bilgisini göstermek için
            ProgressDialog progressDialog
                    = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            //   storageReference ın child tanımlama bu yolla ulaşıyor
            StorageReference ref
                    = storageReference
                    .child(
                            "images/"
                                    + UUID.randomUUID().toString());

            // yüklemeye listeners ekleme

            uploadTask = ref.putFile(filePath);
            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }


                    return ref.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        String mUri = downloadUri.toString();

                        reference = FirebaseDatabase.getInstance().getReference("Products").child(category).child(et_productname.getText().toString());

                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("markName", et_productname.getText().toString());
                        hashMap.put("kilo", Integer.parseInt(et_productkilo.getText().toString()));
                        hashMap.put("cost", Integer.parseInt(et_productcost.getText().toString()));
                        hashMap.put("image", mUri);

                        reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    progressDialog.dismiss();
                                    Toast.makeText(AuthProductDetail.this, "Ürün başarı ile eklendi", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(AuthProductDetail.this, AuthorizedProduct.class);
                                    startActivity(i);
                                    finish();
                                }
                            }
                        });
                    } else {

                    }
                }
            });
        }
    }

    private void UpdateProduct() {
        if (filePath != null) {


            ProgressDialog progressDialog
                    = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();


            StorageReference ref
                    = storageReference
                    .child(
                            "images/"
                                    + UUID.randomUUID().toString());


            uploadTask = ref.putFile(filePath);
            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }


                    return ref.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        String mUri = downloadUri.toString();

                        reference = FirebaseDatabase.getInstance().getReference("Products").child(category).child(et_productname.getText().toString());

                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("markName", et_productname.getText().toString());
                        hashMap.put("kilo", Integer.parseInt(et_productkilo.getText().toString()));
                        hashMap.put("cost", Integer.parseInt(et_productcost.getText().toString()));
                        hashMap.put("image", mUri);

                        reference.updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    progressDialog.dismiss();
                                    Toast.makeText(AuthProductDetail.this, "Ürün başarı ile eklendi", Toast.LENGTH_SHORT).show();

                                }
                            }

                        });
                    } else {

                    }
                }
            });
        } else {
            reference = FirebaseDatabase.getInstance().getReference("Products").child(category).child(et_productname.getText().toString());

            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("markName", et_productname.getText().toString());
            hashMap.put("kilo", Integer.parseInt(et_productkilo.getText().toString()));
            hashMap.put("cost", Integer.parseInt(et_productcost.getText().toString()));
            hashMap.put("image", image);


            reference.updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(AuthProductDetail.this, "Ürün başarı ile güncellendi", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
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
                Intent intent = new Intent(AuthProductDetail.this, PaymentsActivity.class);
                startActivity(intent);
                break;

            case R.id.logout:
                intent = new Intent(AuthProductDetail.this, UserLoginActivity.class);
                startActivity(intent);
                mAuth.signOut();
                finish();
                break;
        }
        return true;
    }
}

