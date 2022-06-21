package com.example.patileremama.recylerAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.patileremama.R;
import com.example.patileremama.models.CurrentUser;


import java.util.ArrayList;

public class OtherBagAdapter extends RecyclerView.Adapter<OtherBagAdapter.ViewHolder>{

    private Context context;
    private ArrayList<CurrentUser> bagArrayList;

    public OtherBagAdapter(Context context, ArrayList<CurrentUser> bagArrayList) {
        this.context = context;
        this.bagArrayList = bagArrayList;
    }

    @NonNull
    @Override
    public OtherBagAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Recylerviewin her bir rowu için hangi layoutu kullanacağımızı belirlediğimzi yer
        LayoutInflater inflater=LayoutInflater.from(context);
        View view =inflater.inflate(R.layout.rv_otherbox_row,parent,false);
        return new OtherBagAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OtherBagAdapter.ViewHolder holder, int position) {
        //layoutumzudaki her bir texte gönderdiğimiz dizilerin o rowunun pozisyonundaki değerleri atıyoruz yani tablomuzu oluşturuyourz
        holder.tv_name.setText(bagArrayList.get(position).username+" Adlı kullanıcının kumbarası");

    }

    @Override
    public int getItemCount() {
        return bagArrayList.size();
    }//loop değerleri kadar döndürür

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_name;

        ViewHolder(View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_name);
        }

    }

    public CurrentUser getItemName(int position) {
        return bagArrayList.get(position);
    }

}
