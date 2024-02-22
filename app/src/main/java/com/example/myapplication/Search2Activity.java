package com.example.myapplication;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Database.MyRoomDataBase;
import com.example.myapplication.fragments.ListFragment;
import com.example.myapplication.fragments.MeaningFragment;
import com.example.myapplication.myword.Word;

import org.chromium.net.UrlRequest;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Search2Activity extends AppCompatActivity implements
        MeaningFragment.ListenerOfClas, ProductAdapter.ClickListener,
        ListFragment.DoTheAction {
    public final static String PATH_URL = "https://api.dictionaryapi.dev/api/v2/entries/en/";
    private List<Word> words;
    private ArrayList<Word> currentWord;
    private ProductAdapter adapter;
    private CronetApplication cronetApplication;
    private TextToSpeech tts;
    private boolean isSuccess = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i2 = getIntent();
        String startingLetter = i2.getStringExtra("starting_word");
        tts = new TextToSpeech(this,i -> {
            if (i == TextToSpeech.SUCCESS) isSuccess = true;
        });
        cronetApplication = new CronetApplication(this);
        cronetApplication.onCreate();
        setContentView(R.layout.activity_main);
        AssetManager assetManager = getAssets();
        words = new ArrayList<>(500);
        Log.d("where_I_reach 63", "onCreate: " + "search2");
        try {
            InputStream inputStream = assetManager.open("result.txt");
            InputStreamReader isr = new InputStreamReader(inputStream);
            BufferedReader br = new BufferedReader(isr);
            String line;
            while ((line = br.readLine()) != null) {
                String[] lineSplit = line.split(": " , 2);
                if (lineSplit.length > 1) {
                    String word = lineSplit[0];
                    String meaning = lineSplit[1];
                    Word word1 = new Word(word, "nothing",0,0,"");
                    word1.setInternetMeaning(meaning);
                    words.add(word1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        EditText input = findViewById(R.id.search_edit_text);
        ImageButton goToSetting = findViewById(R.id.bring_menu);
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
            }
        });
        currentWord = new ArrayList<>();
        currentWord.addAll(words);
        RecyclerView recyclerView = findViewById(R.id.rec);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ProductAdapter(this, currentWord,false);
        recyclerView.setAdapter(adapter);
        goToSetting.setOnClickListener(this::onClick);
        input.setText(startingLetter);
    }

    private void openSettingFragment() {// TODO implement this method
        ArrayList<String> options = new ArrayList<>();
        options.add("first");
        options.add("second");
        ListFragment fragment = new ListFragment(options);
        fragment.show(getSupportFragmentManager(),"no_tag");
    }

    private void updateWord(String input) {

    }


    private void openFragment(Word word) {
        MeaningFragment meaningFragment = new MeaningFragment(word.getWordItself(),
                word.getMeaning(), word.getNumber());
        meaningFragment.show(getSupportFragmentManager(), "my fragment");
    }


    @Override
    public void starWord(String word) {
        for (Word word1 : words) {
            if (word1.getWordItself().equals(word)) {
                word1.increaseNumber();
                break;
            }
        }
    }

    @Override
    public void deleteWord(int index, Word word) {

    }

    @Override
    public void editWord(int index) {
        String stringOfWord = words.get(index).wordItself;
//        ChangeMeaningFragment changeMeaningFragment = new ChangeMeaningFragment(words.
//                get(index).wordItself, words.get(index).meaning, words.get(index));
//        changeMeaningFragment.show(getSupportFragmentManager(), "my bag");
        if (isSuccess){
            tts.speak(stringOfWord,TextToSpeech.QUEUE_FLUSH,null,null);
        }else {
            Toast.makeText(this,"unable to do the the task",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void learnWord(int index) {
//        Toast.makeText(this,"learn",Toast.LENGTH_LONG).show();
    }

    @Override
    public void clickWord(int index) {
        Word currentWord = words.get(index);
        Log.d("word_index", "clickWord: " + currentWord.wordItself);
        Intent i = new Intent(Search2Activity.this, MeaningActivity.class);
        if(currentWord.getInternetMeaning().startsWith("nothing")) {
            MyCallBack mcb = new MyCallBack() {
                @Override
                public void onSucceeded(String meaning) {
                    if (meaning.startsWith("{\"title\":\"No Definitions Found\"")) {
                        openFragment(currentWord);
                        FunctionsStatic.internet_meaning = "a##";
                    } else {
                        i.putExtra("word", meaning);
                        i.putExtra("meaning", currentWord.getMeaning());
                        i.putExtra("word_id", currentWord.id);
                        i.putExtra("number_of", currentWord.getNumber());
                        i.putExtra("category",currentWord.getCategory());// temporary ;need to be changed
                        FunctionsStatic.internet_meaning = meaning;
                        MyHolder.idOfWord = currentWord.id;
                        MyHolder.delta = 0;
                        MyHolder.changingWord = currentWord.getWordItself();
                        MyHolder.isChangedMeaning = false;
                        startActivity(i);
                    }
                }

                @Override
                public void onFailed() {
                    openFragment(currentWord);
                    Toast.makeText(Search2Activity.this,"some string",Toast.LENGTH_LONG).show();
                }
            };
            UrlRequest.Builder builderRequest = cronetApplication.getCronetEngine().
                    newUrlRequestBuilder(PATH_URL + currentWord.wordItself, mcb,
                            cronetApplication.getCronetCallbackExecutorService());
            builderRequest.build().start();
        } else if (currentWord.getInternetMeaning().startsWith("{\"title\":")) {
            openFragment(currentWord);
            Toast.makeText(Search2Activity.this,"another string",Toast.LENGTH_LONG).show();
        }else {
            i.putExtra("word", currentWord.getInternetMeaning());
            i.putExtra("meaning", currentWord.getMeaning());
            i.putExtra("word_id", currentWord.id);
            i.putExtra("number_of", currentWord.getNumber());
            FunctionsStatic.internet_meaning = currentWord.getInternetMeaning();
            MyHolder.idOfWord = currentWord.id;
            MyHolder.delta = 0;
            MyHolder.changingWord = currentWord.getWordItself();
            MyHolder.isChangedMeaning = false;
            startActivity(i);
        }
    }

    @Override
    public int getNumber() {
        return 0;
    }

    @Override
    public void press(String input) {
        Toast.makeText(this,input,Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        currentWord.clear();
        currentWord.addAll(words);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        if (tts != null)
            tts.shutdown();
        super.onDestroy();
    }


    private void onClick(View view) {
        openSettingFragment();
    }
}