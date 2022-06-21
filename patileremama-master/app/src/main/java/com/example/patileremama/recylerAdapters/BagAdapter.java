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

import java.util.ArrayList;

public class BagAdapter extends RecyclerView.Adapter<BagAdapter.ViewHolder>{

    private Context context;
    private ArrayList<Bag> bagArrayList;
    private boolean state;

    public BagAdapter(Context context, ArrayList<Bag> bagArrayList,boolean state) {
        this.context = context;
        this.bagArrayList = bagArrayList;
        this.state = state;
    }

    @NonNull
    @Override
    public BagAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Recylerviewin her bir rowu için hangi layoutu kullanacağımızı belirlediğimzi yer
        LayoutInflater inflater=LayoutInflater.from(context);
        if(state){
            View view =inflater.inflate(R.layout.rv_bag_row,parent,false);
            return new BagAdapter.ViewHolder(view);
        }else{
            View view =inflater.inflate(R.layout.rv_adtobox_row,parent,false);
            return new BagAdapter.ViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull BagAdapter.ViewHolder holder, int position) {
        //layoutumzudaki her bir texte gönderdiğimiz dizilerin o rowunun pozisyonundaki değerleri atıyoruz yani tablomuzu oluşturuyourz

        holder.tv_name.setText(bagArrayList.get(position).markName);
        holder.tv_cost.setText(String.valueOf(bagArrayList.get(position).cost)+" TL");
        if(!state){
            holder.tv_count.setText("Adet :"+String.valueOf(bagArrayList.get(position).count)+" adet");
            holder.tv_kilo.setText(String.valueOf(bagArrayList.get(position).kilo)+" kg");
        }
        Glide.with(context).load(bagArrayList.get(position).image).into(holder.iv_bag);




    }

    @Override
    public int getItemCount() {
        return bagArrayList.size();
    }//loop değerleri kadar döndürür

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_name,tv_count,tv_cost,tv_kilo;
        ImageView iv_bag;


        ViewHolder(View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_name);
            if(!state){
                tv_kilo = itemView.findViewById(R.id.tv_kilo);
                tv_count = itemView.findViewById(R.id.tv_count);
            }
            tv_cost = itemView.findViewById(R.id.tv_cost);
            iv_bag = itemView.findViewById(R.id.iv_bag);

        }

    }

    public Bag getItemName(int position) {
        return bagArrayList.get(position);
    }


}

