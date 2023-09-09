
package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private final Context mCtx;
    private final List<Word> wordList;
    public ProductAdapter(Context mCtx, List<Word> wordList) {
        this.mCtx = mCtx;
        this.wordList = wordList;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.word_box, null);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        Word word = wordList.get(position);
        holder.wordString.setText(word.getWordItself());
    }


    @Override
    public int getItemCount() {
        return wordList.size();
    }


    static class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView wordString;

        public ProductViewHolder(View itemView) {
            super(itemView);
            wordString = itemView.findViewById(R.id.textView2);
        }
    }
}