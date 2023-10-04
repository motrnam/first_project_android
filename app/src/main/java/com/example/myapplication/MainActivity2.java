package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.chromium.net.UrlRequest;
import org.chromium.net.UrlResponseInfo;

import java.util.ArrayList;
import java.util.List;

public class MainActivity2 extends AppCompatActivity {
    private LayoutInflater inflater;
    private static final String THE_PATH = "https://api.dictionaryapi.dev/media/pronunciations/en/";

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
                ib.setOnClickListener(view1 ->{
                    SecondCallBack secondCallBack = new SecondCallBack() {
                        @Override
                        public void onFailed() {
                            Toast.makeText(MainActivity2.this,"unable to receive meaning",
                                    Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onSucceeded(UrlRequest request, UrlResponseInfo info, byte[] bodyBytes) {
                            final Bitmap bitmap = BitmapFactory.decodeByteArray(bodyBytes,
                                    0,bodyBytes.length);
//                            MediaPlayer mediaPlayer = MediaPlayer.create(MainActivity2.this,)
                        }
                    };
                });
            }catch (Exception e){
                Toast.makeText(this,"some bug stopped the program please contact me "
                        ,Toast.LENGTH_LONG).show();
                finish();
            }
        }else
            finish();
    }
}