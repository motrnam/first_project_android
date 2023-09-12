package com.example.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Database.MyRoomDataBase;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MyFragment.MyClickListener,
        MeaningFragment.ListenerOfClas{
    private RecyclerView recyclerView;
    private List<Word> words;
    private LayoutInflater inflater;
    private LinearLayout layout;
    private ArrayList<Word> currentWord;
    private MyRoomDataBase roomDataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        layout = findViewById(R.id.line_lay);
//        words.add(new Word("booksssssssssssss","ketab",3,words.size()));
//        words.add(new Word("look","negah",2,words.size()));
//        words.add(new Word("cook","Ghaza",5,words.size()));
        roomDataBase = MyRoomDataBase.getInstance(this);
        words = roomDataBase.mainDataAccess().getAll();
        ImageView iv = findViewById(R.id.imageButton2);
        iv.setOnClickListener(view -> {
            addNewWord();
        });
        EditText input = findViewById(R.id.search_edit_text);
        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                updateWord(editable.toString());
                updateUI();
            }
        });
//        recyclerView = findViewById(R.id.rec);
//        recyclerView.setAutoHandwritingEnabled(false);
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        ProductAdapter adapter = new ProductAdapter(this,words);
//        recyclerView.setAdapter(adapter);
        inflater = getLayoutInflater();
        currentWord = new ArrayList<>();
        for(Word word:words){
            addWord(word);
        }
    }

    private void updateWord(String input) {
        currentWord = new ArrayList<>();
        for (Word word:words){
            if (word.getWordItself().startsWith(input))
                currentWord.add(word);
            if (currentWord.size() >= 10)
                return;
        }
    }

    private void updateUI(){
//        long start = System.currentTimeMillis();
        layout.removeAllViews();
        for (Word word :currentWord){
            addWord(word);
        }
//        long end = System.currentTimeMillis();
//        Toast.makeText(this,String.valueOf(end - start),Toast.LENGTH_LONG).show();
    }

    private void addNewWord() {
        Toast.makeText(this,"ali",Toast.LENGTH_LONG).show();
        MyFragment myFragment = new MyFragment();
        myFragment.show(getSupportFragmentManager(),"example");
    }

    private void addWord(Word word){
        View cardView =  inflater.inflate(R.layout.word_box,null);
        TextView textView = cardView.findViewById(R.id.textView2);
        textView.setText(word.getWordItself());
        cardView.setOnClickListener(view -> openFragment(word));
        layout.addView(cardView);
    }

    private void openFragment(Word word) {
        MeaningFragment meaningFragment = new MeaningFragment(word.getWordItself(),
                word.getMeaning(),word.getNumber(),word.getIndex());
        meaningFragment.show(getSupportFragmentManager(),"my fragment");
    }

    @Override
    public void ok_clicked(String word, String meaning) {
        for (Word word1:words){
            if (word1.getMeaning().equals(word)){
                Toast.makeText(this,"you have already add that work",
                        Toast.LENGTH_LONG).show();
                return;
            }
        }
        Word newWord = new Word(word,meaning,0,words.size());
        roomDataBase.mainDataAccess().insert(newWord);
        words.add(newWord);
        addWord(newWord);
    }

    @Override
    public void starWord(int indexOF) {
        if (indexOF < words.size())
            words.get(indexOF).increaseNumber();
    }
}