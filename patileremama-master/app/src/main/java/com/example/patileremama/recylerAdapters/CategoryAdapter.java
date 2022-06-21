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

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder>{
private Context context;
private ArrayList<Category> categoryList;

public CategoryAdapter(Context context, ArrayList<Category> categoryList) {
        this.context = context;
        this.categoryList = categoryList;
        }

@NonNull
@Override
public CategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Recylerviewin her bir rowu için hangi layoutu kullanacağımızı belirlediğimzi yer
        LayoutInflater inflater=LayoutInflater.from(context);
        View view =inflater.inflate(R.layout.rv_category_row,parent,false);
        return new CategoryAdapter.ViewHolder(view);
        }

@Override
public void onBindViewHolder(@NonNull CategoryAdapter.ViewHolder holder, int position) {
        //layoutumzudaki her bir texte gönderdiğimiz dizilerin o rowunun pozisyonundaki değerleri atıyoruz yani tablomuzu oluşturuyourz
        holder.categorytext.setText(categoryList.get(position).name);




        }

@Override
public int getItemCount() {
        return categoryList.size();
        }//loop değerleri kadar döndürür

public class ViewHolder extends RecyclerView.ViewHolder {

    TextView categorytext;


    ViewHolder(View itemView) {
        super(itemView);
        categorytext = itemView.findViewById(R.id.tv_category);
    }

}

    public Category getItemName(int position) {
        return categoryList.get(position);
    }


}

