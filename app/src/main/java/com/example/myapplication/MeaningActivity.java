package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MeaningActivity extends AppCompatActivity {
    private LayoutInflater inflater;
    private static final String BASE_URL = "https://api.dictionaryapi.dev/media/pronunciations/en/";
    private ConnectivityManager conMgr;
    private final static Executor executor = Executors.newSingleThreadExecutor();
    private TextToSpeech textToSpeech;
    private boolean stateOf = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                stateOf = (i == TextToSpeech.SUCCESS);
            }
        });
        setContentView(R.layout.activity_main2);
        LinearLayout up = findViewById(R.id.up_view);
        inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.show_word_main_part,null);
        TextView textViewWord = view.findViewById(R.id.theWordItself);
        up.addView(view);
        RecyclerView recyclerView = findViewById(R.id.my_recyclerView);
        Intent i = getIntent();
        String wordString = i.getStringExtra("word");
        String theMeaning = i.getStringExtra("meaning");
        if (wordString != null){
            try {
                List<WordFromInternet> fwfi = (FunctionsStatic.getFinalByString(wordString)).all;
                List<WordDefinition> definitions = new ArrayList<>();
                for (WordFromInternet word : fwfi) {
                    for (WordMeaning wordMeaning : word.meanings)
                        definitions.addAll(wordMeaning.definitions);
                }
                String result = fwfi.get(0).word.toString() + " : " + theMeaning;
                textViewWord.setText(result);
                TextView pro = findViewById(R.id.pronucitoon);
                pro.setText(fwfi.get(0).phonetic);
                WordAdapter adapter = new WordAdapter(definitions, this);
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                recyclerView.setAdapter(adapter);
                ImageButton ib = view.findViewById(R.id.speaker);
                ib.setOnClickListener(view1 -> {
                    executor.execute(() -> {
                        if (conMgr.getActiveNetworkInfo() == null){
                            Toast.makeText(MeaningActivity.this,"No access to internet",
                                    Toast.LENGTH_LONG).show();
                        }else {
                            speak(wordString);
                        }
                    });
                });
            }catch (Exception e){
                Toast.makeText(this,"some bug stopped the program please contact me "
                        ,Toast.LENGTH_LONG).show();
                finish();
            }
        }else
            finish();
    }

    private void speak(String word){
        if (stateOf){
            textToSpeech.speak(word,TextToSpeech.QUEUE_FLUSH,null);
        }else {
            Toast.makeText(this,"some error",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (textToSpeech != null)
            textToSpeech.shutdown();
    }
}