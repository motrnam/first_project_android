
package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.myword.Word;

import java.util.List;


public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private final Context mCtx;
    private final List<Word> wordList;
    private final ClickListener cl;
    public ProductAdapter(Context mCtx, List<Word> wordList) {
        this.mCtx = mCtx;
        this.wordList = wordList;
        try {
            cl = (ClickListener) mCtx;
        }catch (ClassCastException castException){
            throw new ClassCastException("implement this class!!");
        }
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.new_word_box, parent,false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        Word word = wordList.get(position);
        holder.wordString.setText(word.getWordItself());
        int size = cl.getNumber();
        holder.editImageView.setOnClickListener(view -> cl.editWord(position + size));
        holder.deleteWord.setOnClickListener(view -> cl.deleteWord(position + size,word));
        holder.cardView.setOnClickListener(view -> cl.clickWord(position + size));
        holder.reviewWord.setOnClickListener(view -> cl.clickWord(position + size));
    }


    @Override
    public int getItemCount() {
        return wordList.size();
    }


    static class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView wordString;
        CardView cardView;
        ImageView editImageView;
        ImageView deleteWord;
        ImageView reviewWord;

        public ProductViewHolder(View itemView) {
            super(itemView);
            wordString = itemView.findViewById(R.id.tv_name);
            editImageView = itemView.findViewById(R.id.edit_word);
            deleteWord = itemView.findViewById(R.id.delete_word);
            cardView = itemView.findViewById(R.id.card_view);
            reviewWord = itemView.findViewById(R.id.review_word);
        }
    }

    public interface ClickListener{
        void deleteWord(int index,Word theWord);
        void editWord(int index);
        void learnWord(int index);
        void clickWord(int index);
        int getNumber();
    }
}