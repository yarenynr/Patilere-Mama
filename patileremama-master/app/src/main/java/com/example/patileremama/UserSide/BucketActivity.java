package com.example.patileremama.UserSide;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.patileremama.R;
import com.example.patileremama.models.Bucket;
import com.example.patileremama.recylerAdapters.BagAdapter;
import com.example.patileremama.recylerAdapters.BucketAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BucketActivity extends AppCompatActivity {
    int totalCost=0;
    String name="";
    ArrayList<Bucket> bucketArrayList;
    private FirebaseAuth fAuth;
    ArrayList<Integer> priceList;
    ArrayList<String> nameList;
    ArrayList<String> addressList;
    ArrayList<String> markList;
    Bucket bucket;
    BucketAdapter bucketsAdapter;
    RecyclerView recyclerView;
    FirebaseDatabase dbUser;
    FirebaseDatabase dbProduct,dbProcess;// Database e ulaşmamızı sağlayan sınıftan nesne oluşturduk
    private FirebaseDatabase db,dbBucket;// Database e ulaşmamızı sağlayan sınıftan nesne oluşturduk
    private DatabaseReference dbRef,dbBucketRef; // databasede hangi path e ulaşacağımızı belirlediğimiz sınıftan nesne oluşturduk
    private FirebaseUser fUser; // firebasein kullanıcı sınıfından bir nesne oluşturduk current user değerlerini alabilmek için
    String selectedIndex;
    TextView tv_sepet,tv_totalCost;
    Button btn_buy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bucket);
        fAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();// veritabanına erişim sağladık
        dbBucket = FirebaseDatabase.getInstance();// veritabanına erişim sağladık
        dbProcess = FirebaseDatabase.getInstance();
        dbProduct = FirebaseDatabase.getInstance();
        fUser = FirebaseAuth.getInstance().getCurrentUser(); // şuanki kullanıcının verilerini firebaseuser classının nesnesinin içine attık
        recyclerView=findViewById(R.id.rv_bucket);
        bucketArrayList = new ArrayList<>();
        priceList = new ArrayList<>();
        nameList = new ArrayList<>();
        addressList = new ArrayList<>();
        markList = new ArrayList<>();
        tv_sepet=findViewById(R.id.tv_sepet);
        tv_totalCost=findViewById(R.id.tv_totalCost);
        btn_buy=findViewById(R.id.btn_buy);
        StoreDataList();
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));


        //listenin elemanlarının click fonksiyonları

        recyclerView.addOnItemTouchListener(new CategoryActivity.RecyclerItemClickListener(getApplicationContext(), recyclerView, new CategoryActivity.RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }

            @Override
            public void onLongItemClick(View view, int position) {
                selectedIndex=String.valueOf(position);
                bucket= bucketsAdapter.getItemName(position);
                selectIslem();
            }
        }));

        btn_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog();
            }
        });

    }
    void alertDialog() {
        // alertdialog un viewının gelmesi içinn
        LayoutInflater li = LayoutInflater.from(BucketActivity.this);
        View alertDialogView = li.inflate(R.layout.alert_dialog, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                BucketActivity.this);

        // içeriğini doldurmak için
        alertDialogBuilder.setView(alertDialogView);

        final EditText card_no = (EditText) alertDialogView.findViewById(R.id.card_no);
        final EditText card_lateDate = (EditText) alertDialogView.findViewById(R.id.card_lateDate);
        final EditText card_cvcode = (EditText) alertDialogView.findViewById(R.id.card_cvcode);
        final EditText card_name = (EditText) alertDialogView.findViewById(R.id.card_name);

        // alertdialog mesaj bilgisi verilmesi için
        alertDialogBuilder
                .setCancelable(false) // dış ekrana dokunludğunda kapanmasın diye
                .setPositiveButton("Satın Al", new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    public void onClick(DialogInterface dialog, int id) {
                        if(!card_no.getText().toString().isEmpty()&&!card_lateDate.getText().toString().isEmpty()&&!card_cvcode.getText().toString().isEmpty()&&!card_name.getText().toString().isEmpty()){

                            DatabaseReference  dbProcessRef = dbProcess.getReference("Process").child(fUser.getUid()).push();
                            HashMap<String, Object> hashMapProcess = new HashMap<>();
                            hashMapProcess.put("boxOwnerAddress", String.join(",", addressList));
                            hashMapProcess.put("boxOwnerName", String.join(",", nameList));
                            hashMapProcess.put("name", fUser.getDisplayName());
                            hashMapProcess.put("markName", String.join(",", markList));
                            hashMapProcess.put("cost", totalCost);
                            dbProcessRef.setValue(hashMapProcess).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        db.getReference("Buckets").child(fUser.getUid()).removeValue();
                                        tv_totalCost.setText("0");
                                        Toast.makeText(BucketActivity.this, "Ödemeniz Alındı.", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });

                        }else{
                            Toast.makeText(BucketActivity.this, "Lütfen tüm alanları doldurunuz", Toast.LENGTH_SHORT).show();
                        }

                    }
                })
                .setNegativeButton("İptal",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        // alert dialog oluşturma
        AlertDialog alertDialog = alertDialogBuilder.create();

        // göstermesi için
        alertDialog.show();
    }
    public boolean containsName(final List<String> list, final String name){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return list.stream().filter(o -> o.equals(name)).findFirst().isPresent();
        }else{
            return true;
        }
    }

    //Ürünleri çağırdığımız yer
    private void StoreDataList() {
        dbRef = db.getReference("Buckets/"+fUser.getUid());
        dbRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                bucketArrayList.clear();
                priceList.clear();
                nameList.clear();
                addressList.clear();
                markList.clear();
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    Bucket task=ds.getValue(Bucket.class);
                    int price=task.cost*task.count;
                    priceList.add(price);
                    bucketArrayList.add(task);
                    if(!containsName(nameList,task.boxName) && !containsName(addressList,task.address)){
                       nameList.add(task.boxName);
                       addressList.add(task.address);
                       markList.add(task.boxMarkName);
                    }

                }

                bucketsAdapter =new BucketAdapter(getApplicationContext(), bucketArrayList);
                bucketsAdapter.notifyDataSetChanged();
                recyclerView.setAdapter(bucketsAdapter);
                if(bucketArrayList.size()!=0){
                    for (int i:priceList) {
                        totalCost=i+totalCost;
                    }

                    tv_totalCost.setText("Ücret = "+String.valueOf(totalCost));
                }

                tv_sepet.setText("Sepette "+bucketArrayList.size()+" ürün bulunmakta");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void selectIslem() {
        final CharSequence[] options = {"Sil","Güncelle"};
        AlertDialog.Builder builder = new AlertDialog.Builder(BucketActivity.this);
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Sil"))
                {
                    db.getReference("Buckets").child(fUser.getUid()).child(bucket.boxMarkName).removeValue();
                    Toast.makeText(BucketActivity.this, "Ürün Silindi", Toast.LENGTH_SHORT).show();

                    bucketsAdapter.notifyDataSetChanged();//sayfayı yenilemeye yarar(recylerview i yeniler)
                }
                else if (options[item].equals("Güncelle"))
                {


                    // sadece miktar değiştirebilir veya ürünü siler
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(BucketActivity.this);
                    final EditText edittext = new EditText(BucketActivity.this);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT);
                    edittext.setLayoutParams(lp);
                    alertDialog.setView(edittext);
                    alertDialog.setMessage("Lütfen Adet Giriniz");
                    alertDialog.setTitle("Sepeti Güncelle");


                    alertDialog.setPositiveButton("Güncelle", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {

                            int YourEditTextValue = Integer.parseInt(edittext.getText().toString());
                            if (YourEditTextValue > 0) {
                                totalCost=0;
                                dbBucketRef = dbBucket.getReference("Buckets/" + fUser.getUid()+"/"+bucket.boxMarkName);
                                HashMap<String, Object> hashMap = new HashMap<>();
                                hashMap.put("count", YourEditTextValue);
                                dbBucketRef.updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {

                                            Toast.makeText(getApplicationContext(), "Ürün güncellendi!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }else{
                                Toast.makeText(getApplicationContext(), "Lütfen geçerli bir adet giriniz!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    alertDialog.setNegativeButton("İptal", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            // what ever you want to do with No option.
                        }
                    });

                    alertDialog.show();

                    dialog.dismiss();

                }
            }
        });
        builder.show();
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
                Intent intent = new Intent(BucketActivity.this, ProfileActivity.class);
                startActivity(intent);
                break;

            case R.id.mybox:
                intent = new Intent(BucketActivity.this, MyBoxActivity.class);
                startActivity(intent);
                break;

            case R.id.logout:
                intent = new Intent(BucketActivity.this, UserLoginActivity.class);
                startActivity(intent);
                fAuth.signOut();
                finish();
                break;
        }
        return true;
    }

}