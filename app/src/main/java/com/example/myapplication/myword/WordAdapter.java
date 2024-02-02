package com.example.myapplication.myword;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.util.List;

public class WordAdapter extends RecyclerView.Adapter<WordAdapter.WordViewHolder> {
    private final Context c;
    List<WordDefinition> wordDefinitionList;
    private final Listener listener;

    public WordAdapter(List<WordDefinition> wordDefinitionList, Context c) {
        this.c = c;
        this.wordDefinitionList = wordDefinitionList;
        try {
            listener = (Listener) c;
        } catch (ClassCastException exception) {
            throw new ClassCastException("please implement the interface!");
        }
    }

    @NonNull
    @Override
    public WordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(c);
        View view = inflater.inflate(R.layout.part_of_word, parent, false);
        return new WordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WordViewHolder holder, int position) {
        WordDefinition definition = wordDefinitionList.get(position);
        holder.syn.setText(definition.getSynonymsInStringFormat());
        holder.ac.setText(definition.getAntonymsInStringFormat());
        holder.def.setText(definition.getDefinition());
        holder.example.setText(definition.getExample());
        holder.def.setOnClickListener(view -> listener.copyToClipboard(definition.getDefinition()));
        if (definition.getExample() != null && !definition.getExample().equals(""))
            holder.example.setOnClickListener(view -> listener.copyToClipboard(definition.getExample()));
    }

    @Override
    public int getItemCount() {
        return wordDefinitionList.size();
    }

    public class WordViewHolder extends RecyclerView.ViewHolder {
        TextView def, example, syn, ac;

        public WordViewHolder(@NonNull View itemView) {
            super(itemView);
            def = itemView.findViewById(R.id.def_word_box);
            example = itemView.findViewById(R.id.example_word);
            syn = itemView.findViewById(R.id.syn);
            ac = itemView.findViewById(R.id.textView5);
        }
    }

    public interface Listener {
        void copyToClipboard(String text);
    }
}
