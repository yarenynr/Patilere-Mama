package com.example.patileremama.recylerAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.patileremama.R;
import com.example.patileremama.models.Product;

import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder>{

    private Context context;
    private ArrayList<Product> productArrayList;

public ProductAdapter(Context context, ArrayList<Product> productArrayList) {
        this.context = context;
        this.productArrayList = productArrayList;
        }

@NonNull
@Override
public ProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Recylerviewin her bir rowu için hangi layoutu kullanacağımızı belirlediğimzi yer
        LayoutInflater inflater=LayoutInflater.from(context);
        View view =inflater.inflate(R.layout.rv_product_row,parent,false);
        return new ProductAdapter.ViewHolder(view);
        }

@Override
public void onBindViewHolder(@NonNull ProductAdapter.ViewHolder holder, int position) {
        //layoutumzudaki her bir texte gönderdiğimiz dizilerin o rowunun pozisyonundaki değerleri atıyoruz yani tablomuzu oluşturuyourz
        holder.categorytext.setText(productArrayList.get(position).markName);
        Glide.with(context).load(productArrayList.get(position).image).into(holder.iv_product);




        }

@Override
public int getItemCount() {
        return productArrayList.size();
        }//loop değerleri kadar döndürür

public class ViewHolder extends RecyclerView.ViewHolder {

    TextView categorytext;
    ImageView iv_product;


    ViewHolder(View itemView) {
        super(itemView);
        categorytext = itemView.findViewById(R.id.tv_category);
        iv_product = itemView.findViewById(R.id.iv_product);
    }

}

    public Product getItemName(int position) {
        return productArrayList.get(position);
    }


}

