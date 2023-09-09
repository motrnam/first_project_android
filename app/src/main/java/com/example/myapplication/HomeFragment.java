package com.example.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HomeFragment extends Fragment implements MyFragment.MyClickListener{
    private RecyclerView recyclerView;
    private ArrayList<Word> words;
    private LayoutInflater inflater;
    private LinearLayout layout;
    private ArrayList<Word> currentWord;
    private AppCompatActivity activity;

    public HomeFragment(AppCompatActivity activity){
        this.activity = activity;
        this.inflater = activity.getLayoutInflater();
    }

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        Activity activity = this;
//        setContentView(R.layout.activity_main);
////        layout = findViewById(R.id.line_lay);
////        words = new ArrayList<>();
////        words.add(new Word("booksssssssssssss","ketab",7));
////        words.add(new Word("look","negah",6));
////        words.add(new Word("cook","Ghaza",6));
//        ImageView iv = findViewById(R.id.imageButton2);
//        iv.setOnClickListener(view -> addNewWord());
//        EditText input = findViewById(R.id.search_edit_text);
//        input.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//                updateWord(editable.toString());
//                updateUI();
//            }
//        });
////        recyclerView = findViewById(R.id.rec);
////        recyclerView.setAutoHandwritingEnabled(false);
////        recyclerView.setHasFixedSize(true);
////        recyclerView.setLayoutManager(new LinearLayoutManager(this));
////        ProductAdapter adapter = new ProductAdapter(this,words);
////        recyclerView.setAdapter(adapter);
//        inflater = getLayoutInflater();
//        currentWord = new ArrayList<>();
//        for(Word word:words){
//            addWord(word);
//        }
//    }

    private void updateWord(String input) {
        currentWord = new ArrayList<>();
        for (Word word:words){
            if (word.getWordItself().startsWith(input))
                currentWord.add(word);
            if (currentWord.size() >= 10)
                return;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment,container,false);
        layout = view.findViewById(R.id.line_lay);
        words = new ArrayList<>();
//        words.add(new Word("booksssssssssssss","ketab",7));
//        words.add(new Word("look","negah",6));
//        words.add(new Word("cook","Ghaza",6));
        ImageView iv = view.findViewById(R.id.imageButton2);
        iv.setOnClickListener(view5 -> addNewWord());
        EditText input = view.findViewById(R.id.search_edit_text);
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
        return view;
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
        MyFragment myFragment = new MyFragment();
        myFragment.show(activity.getSupportFragmentManager(),"example");
    }

    private void addWord(Word word){
        View cardView =  inflater.inflate(R.layout.word_box,null);
        TextView textView = cardView.findViewById(R.id.textView2);
        textView.setText(word.getWordItself());
        layout.addView(cardView);
    }

    @Override
    public void ok_clicked(String word, String meaning) {
        for (Word word1:words){
            if (word1.getMeaning().equals(word)){
                Toast.makeText(activity,"you have already add that work",
                        Toast.LENGTH_LONG).show();
                return;
            }
        }
//        Word newWord = new Word(word,meaning,7);
//        words.add(newWord);
//        addWord(newWord);
    }
}