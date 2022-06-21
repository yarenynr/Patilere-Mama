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
import com.example.patileremama.models.Bag;
import com.example.patileremama.models.Bucket;

import java.util.ArrayList;

public class BucketAdapter extends RecyclerView.Adapter<BucketAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Bucket> bucketArrayList;

    public BucketAdapter(Context context, ArrayList<Bucket> bucketArrayList) {
        this.context = context;
        this.bucketArrayList = bucketArrayList;
    }

    @NonNull
    @Override
    public BucketAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Recylerviewin her bir rowu için hangi layoutu kullanacağımızı belirlediğimzi yer
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.rv_adtobox_row, parent, false);
        return new BucketAdapter.ViewHolder(view);


    }

    @Override
    public void onBindViewHolder(@NonNull BucketAdapter.ViewHolder holder, int position) {
        //layoutumzudaki her bir texte gönderdiğimiz dizilerin o rowunun pozisyonundaki değerleri atıyoruz yani  tablomuzu oluşturuyourz

        holder.tv_name.setText(bucketArrayList.get(position).boxMarkName);
        holder.tv_cost.setText(String.valueOf(bucketArrayList.get(position).cost) + " TL");
        holder.tv_count.setText("Sepette :"+String.valueOf(bucketArrayList.get(position).count)+" tane");
        holder.tv_kilo.setText(String.valueOf(bucketArrayList.get(position).kilo)+" kg");

        Glide.with(context).load(bucketArrayList.get(position).image).into(holder.iv_bag);


    }

    @Override
    public int getItemCount() {
        return bucketArrayList.size();
    }//loop değerleri kadar döndürür

    public class ViewHolder extends RecyclerView.ViewHolder {
        //Define textviews
        TextView tv_name, tv_count, tv_cost, tv_kilo;
        ImageView iv_bag;


        ViewHolder(View itemView) {//DEFINE TEXTVİEWS AND BUTTON WITH XML FILES WITH ID
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_cost = itemView.findViewById(R.id.tv_cost);
            tv_count = itemView.findViewById(R.id.tv_count);
            tv_kilo = itemView.findViewById(R.id.tv_kilo);
            iv_bag = itemView.findViewById(R.id.iv_bag);

        }

    }
    public Bucket getItemName(int position) {
        return bucketArrayList.get(position);
    }
}