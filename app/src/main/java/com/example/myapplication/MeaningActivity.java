package com.example.myapplication;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Database.MyRoomDataBase;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MeaningActivity extends AppCompatActivity implements WordAdapter.Listener,
        ChangeMeaningFragmentCopy.MyClickListener2 {
    private LayoutInflater inflater;
    private ConnectivityManager conMgr;
    private final static Executor executor = Executors.newSingleThreadExecutor();
    private TextToSpeech textToSpeech;
    private boolean stateOfTTS = false;
    private TextView textViewWord;
    private Handler handler;
    private MainActivity last;
    private int wordId,numberOf;
    private ImageButton inc;
    private ProgressBar pb;
    private MyRoomDataBase roomDataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        roomDataBase = MyRoomDataBase.getInstance(this);
        conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        handler = new Handler();
        textToSpeech = new TextToSpeech(this, i -> stateOfTTS = (i == TextToSpeech.SUCCESS));
        setContentView(R.layout.activity_main2);
        LinearLayout up = findViewById(R.id.up_view);
        inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.show_word_main_part, null);
        textViewWord = view.findViewById(R.id.theWordItself);
        up.addView(view);
        RecyclerView recyclerView = findViewById(R.id.my_recyclerView);
        Intent i = getIntent();
        String wordString = i.getStringExtra("word");
        String theMeaning = i.getStringExtra("meaning");
        wordId = i.getIntExtra("word_id",0);
        numberOf = i.getIntExtra("number_of",0);
        pb = findViewById(R.id.learn_amount);
        pb.setProgress(((int) numberOf * 100 /7),true);
        if (wordString != null) {
            try {
                List<WordFromInternet> fwfi = (FunctionsStatic.getFinalByString(wordString)).all;
                List<WordDefinition> definitions = new ArrayList<>();
                for (WordFromInternet word : fwfi) {
                    for (WordMeaning wordMeaning : word.meanings)
                        definitions.addAll(wordMeaning.definitions);
                }
                String result = fwfi.get(0).word + " : " + theMeaning;
                textViewWord.setOnClickListener(view1 -> copyToClipboard(theMeaning));
                textViewWord.setText(result);
                TextView pro = findViewById(R.id.pronucitoon);
                pro.setText(fwfi.get(0).phonetic);
                WordAdapter adapter = new WordAdapter(definitions, this);
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                recyclerView.setAdapter(adapter);
                ImageButton ib = view.findViewById(R.id.speaker);
                inc = findViewById(R.id.learn_the_word);
                ImageButton myIB = findViewById(R.id.imageButton);
                myIB.setOnClickListener(view1 -> openFragment(fwfi.get(0).word));
                ib.setOnClickListener(view1 -> executor.execute(() -> {
                    if (conMgr.getActiveNetworkInfo() == null) {
                        handler.post(() -> Toast.makeText(MeaningActivity.this, "No access to internet",
                                Toast.LENGTH_LONG).show());

                    } else {
                        speak(fwfi.get(0).word);//word itself
                    }
                }));
            } catch (Exception e) {
                Toast.makeText(this, "some bug stopped the program please contact me "
                        , Toast.LENGTH_LONG).show();
                finish();
            }
            try {// this class should only called after MainActivity
                last = (MainActivity) getParent();//last null
                Log.i("tag", "onCreate::: " + (last == null));
            } catch (ClassCastException castException) {
                finish();
            }
        } else
            finish();
    }

    private void speak(String word) {
        if (stateOfTTS) {
            textToSpeech.speak(word, TextToSpeech.QUEUE_FLUSH, null);
        } else {
            Toast.makeText(this, "some error", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (textToSpeech != null)
            textToSpeech.shutdown();
    }

    @Override
    public void copyToClipboard(String text) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("label", text);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(this, "definition copied to clipboard successfully",
                Toast.LENGTH_LONG).show();
    }

    private void openFragment(String wordItself) {
        ChangeMeaningFragmentCopy copy = new ChangeMeaningFragmentCopy(wordItself, "");
        copy.show(getSupportFragmentManager(), "tag");
    }

    @Override
    public void btn_click_change(String newMeaning, String wordString) {
        if (wordId != 0){
            roomDataBase.mainDataAccess().update(wordId,wordString,newMeaning,numberOf);
        }else
            Toast.makeText(this,"unable to delete the word",Toast.LENGTH_LONG).show();
    }
}