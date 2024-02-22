package com.example.myapplication.fragments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>{
    String[] categories;
    private final Context c;
    private final MyCategoryFragment myCategoryFragment;
    public CategoryAdapter(String[] categories, Context c,MyCategoryFragment myCategoryFragment){
        this.categories = categories;
        this.c = c;
        this.myCategoryFragment = myCategoryFragment;
    }
    @NonNull
    @Override
    public CategoryAdapter.CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(c);
        View view = inflater.inflate(R.layout.category_card, parent, false);
        return new CategoryAdapter.CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.CategoryViewHolder holder, int position) {
        holder.textView.setText(categories[position]);
        holder.textView.setOnClickListener(view -> {
            if (c instanceof MyClickListener){
                ((MyClickListener) c).btn_click(categories[position]);
                myCategoryFragment.dismiss();
            }else {
                throw new ClassCastException("please implement the required class! " + c.getClass().getName());
            }
        });
    }

    @Override
    public int getItemCount() {
        return categories.length;
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView);
        }
    }

    public interface MyClickListener{
        void btn_click(String category);
    }
}
