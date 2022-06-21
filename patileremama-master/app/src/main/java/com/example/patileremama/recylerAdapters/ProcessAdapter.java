package com.example.patileremama.recylerAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.patileremama.R;
import com.example.patileremama.models.Category;
import com.example.patileremama.models.Process;

import java.util.ArrayList;

public class ProcessAdapter extends RecyclerView.Adapter<ProcessAdapter.ViewHolder>{
    private Context context;
    private ArrayList<Process> processArrayList;

    public ProcessAdapter(Context context, ArrayList<Process> processArrayList) {
        this.context = context;
        this.processArrayList = processArrayList;
    }

    @NonNull
    @Override
    public ProcessAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Recylerviewin her bir rowu için hangi layoutu kullanacağımızı belirlediğimzi yer
        LayoutInflater inflater=LayoutInflater.from(context);
        View view =inflater.inflate(R.layout.rv_process_row,parent,false);
        return new ProcessAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProcessAdapter.ViewHolder holder, int position) {
        //layoutumzudaki her bir texte gönderdiğimiz dizilerin o rowunun pozisyonundaki değerleri atıyoruz yani tablomuzu oluşturuyourz
        holder.tv_process.setText(processArrayList.get(position).markName+" adlı ürün ödemesi alındı Gönderen Adı: "+processArrayList.get(position).name+" Gönderilecek Adres: "+processArrayList.get(position).boxOwnerAddress);




    }

    @Override
    public int getItemCount() {
        return processArrayList.size();
    }//loop değerleri kadar döndürür

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_process;


        ViewHolder(View itemView) {
            super(itemView);
            tv_process = itemView.findViewById(R.id.tv_process);
        }

    }

    public Process getItemName(int position) {
        return processArrayList.get(position);
    }


}

