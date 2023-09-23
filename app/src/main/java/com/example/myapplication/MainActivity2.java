package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity2 extends AppCompatActivity {
    private LayoutInflater inflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        LinearLayout up = findViewById(R.id.up_view);
        inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.show_word_main_part,null);
        TextView textViewWord = view.findViewById(R.id.theWordItself);
        up.addView(view);
        RecyclerView recyclerView = findViewById(R.id.my_recyclerView);
        Intent i = getIntent();
        String wordString = i.getStringExtra("word");
        if (wordString != null){
            List<WordFromInternet> fwfi = (FunctionsStatic.getFinalByString(wordString)).all;
            List<WordDefinition> definitions = new ArrayList<>();
            for (WordFromInternet word:fwfi){
                for (WordMeaning wordMeaning:word.meanings)
                    definitions.addAll(wordMeaning.definitions);
            }
            textViewWord.setText(fwfi.get(0).word);
            WordAdapter adapter = new WordAdapter(definitions,this);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(adapter);
        }else
            finish();
    }
}