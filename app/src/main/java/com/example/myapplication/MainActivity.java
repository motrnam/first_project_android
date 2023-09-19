package com.example.myapplication;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Database.MyRoomDataBase;

import org.chromium.net.CronetEngine;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity implements MyFragment.MyClickListener,
        MeaningFragment.ListenerOfClas,ProductAdapter.ClickListener,ChangeMeaningFragment.MyClickListener2{
    private RecyclerView recyclerView;
    public Executor executor;
    private List<Word> words;
    private LayoutInflater inflater;
    private ArrayList<Word> currentWord;
    private MyRoomDataBase roomDataBase;
    private ProductAdapter adapter;
    private ArrayList<Integer> removed = new ArrayList<>(100);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        CronetEngine.Builder myBuilder = new CronetEngine.Builder(this);
        CronetEngine cronetEngine = myBuilder.build();
        executor = Executors.newSingleThreadExecutor();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        roomDataBase = MyRoomDataBase.getInstance(this);
        words = roomDataBase.mainDataAccess().getAll();
        ImageButton ib = findViewById(R.id.click_button);
        ib.setOnClickListener(view1 -> {
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
                if (editable.toString().equals("qwe")){
                    addNewWord();
                    input.setText("");
                }else {
                    updateWord(editable.toString());
                    updateUI();
                }
            }
        });
        currentWord = new ArrayList<>();
        for (int i = 0; i < Math.min(50,words.size()); i++) {
            currentWord.add(words.get(i));
            Toast.makeText(this,"bad call",Toast.LENGTH_LONG).show();
        }
        recyclerView = findViewById(R.id.rec);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ProductAdapter(this,currentWord);
        recyclerView.setAdapter(adapter);
        inflater = getLayoutInflater();
    }

    private void updateWord(String input) {
//        for (Word word:words){
//            if (word.getWordItself().startsWith(input))
//                currentWord.add(word);
//            if (currentWord.size() >= 10)
//                return;
//        }
    }

    private void updateUI(){

    }

    private void addNewWord() {
        Log.i("tag tag", String.valueOf(currentWord.size()));
        Toast.makeText(this,String.valueOf(currentWord.size()) +
                String.valueOf(words.size()),Toast.LENGTH_LONG).show();
        MyFragment myFragment = new MyFragment(this);
        myFragment.show(getSupportFragmentManager(),"example");
    }

    private void addWord(Word word){
//        View cardView =  inflater.inflate(R.layout.word_box,null);
//        TextView textView = cardView.findViewById(R.id.textView2);
//        textView.setText(word.getWordItself());
//        cardView.setOnClickListener(view -> openFragment(word));
////        layout.addView(cardView);

    }

    private void openFragment(Word word) {
        MeaningFragment meaningFragment = new MeaningFragment(word.getWordItself(),
                word.getMeaning(),word.getNumber(),word.getIndex());
        meaningFragment.show(getSupportFragmentManager(),"my fragment");
    }

    public CronetApplication getCronetApplication() {
        return ((CronetApplication) getApplication());
    }

    @Override
    public void ok_clicked(String word, String meaning) {
        for (Word word1:words){
            if (word1.getMeaning().equals(word)){
//                Toast.makeText(this,"you have already add that work",
//                        Toast.LENGTH_LONG).show();
                return;
            }
        }
//        Toast.makeText(this,String.valueOf(adapter.getItemCount()),Toast.LENGTH_LONG).show();
        Word newWord = new Word(word,meaning,0,words.size());
        roomDataBase.mainDataAccess().insert(newWord);
        words.add(newWord);
        currentWord.add(newWord);
        adapter.notifyItemInserted(currentWord.size() - 1);
    }

    @Override
    public String getMeaningFromInternet(String word,MyFragment myFragment) {
        if (executor == null)
            return "@e1";
        executor.execute(new Runnable() {
            @Override
            public void run() {

            }
        });
        return null;
    }

    @Override
    public void starWord(int indexOF) {
        if (indexOF < words.size()) {
            Word w = words.get(indexOF);
            w.increaseNumber();
            roomDataBase.mainDataAccess().update(w.id,w.wordItself,w.getMeaning(),getNumber());
        }
    }

    @Override
    public void deleteWord(int index,Word word) {
//        Toast.makeText(this,String.valueOf(index),Toast.LENGTH_LONG).show();
        int minus = 0;
        for (Integer i :removed){
            if (index >= i)
                minus++;
        }
        removed.add(index);
        index -= minus;
        words.remove(word);
        currentWord.remove(word);
        roomDataBase.mainDataAccess().delete(word);
        adapter.notifyItemRemoved(index);
    }

    @Override
    public void editWord(int index) {
        int minus = 0;
        for (Integer i :removed){
            if (index >= i)
                minus++;
        }
        index -= minus;
        Toast.makeText(this,String.valueOf(index),Toast.LENGTH_LONG).show();
        ChangeMeaningFragment changeMeaningFragment = new ChangeMeaningFragment(words.
                get(index).wordItself,words.get(index).meaning,words.get(index));
        changeMeaningFragment.show(getSupportFragmentManager(),"my bag");
    }

    @Override
    public void learnWord(int index) {
        Toast.makeText(this,"learn",Toast.LENGTH_LONG).show();
    }

    @Override
    public void clickWord(int index) {
        int minus = 0;
        for (Integer i :removed){
            if (index >= i)
                minus++;
        }
        index -= minus;
        openFragment(words.get(index));
    }

    @Override
    public int getNumber() {
        return removed.size();
    }

    @Override
    public void btn_click_change(String newMeaning, Word word1,int index) {
        word1.meaning = newMeaning;
        roomDataBase.mainDataAccess().update(word1.id,word1.wordItself,newMeaning,word1.getNumber());
        int minus = 0;
        for (Integer i :removed){
            if (index >= i)
                minus++;
        }
        index -= minus;
        adapter.notifyItemChanged(index);
    }


}