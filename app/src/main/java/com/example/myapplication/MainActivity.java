package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
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
import com.example.myapplication.fragments.AddMeaningSomethingElse;
import com.example.myapplication.fragments.AskMeaningFragment;
import com.example.myapplication.fragments.ChangeMeaningFragment;
import com.example.myapplication.fragments.ListFragment;
import com.example.myapplication.fragments.MeaningFragment;
import com.example.myapplication.fragments.MyFragment;
import com.example.myapplication.myword.Word;

import org.chromium.net.UrlRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity implements MyFragment.MyClickListener,
        MeaningFragment.ListenerOfClas, ProductAdapter.ClickListener,
        ChangeMeaningFragment.MyClickListener2, AskMeaningFragment.TheListener,
        ListFragment.DoTheAction {
    public final static String PATH_URL = "https://api.dictionaryapi.dev/api/v2/entries/en/";
    public Executor executor;
    private List<Word> words;
    private ArrayList<Word> currentWord;
    private MyRoomDataBase roomDataBase;
    private ProductAdapter adapter;
    private CronetApplication cronetApplication;
    private final ArrayList<Integer> removed = new ArrayList<>(100);
    private Handler handler;
    private ConnectivityManager conMgr;
    private TextToSpeech tts;
    private boolean isSuccess = false;
    private Word currentViewWord;
    private String category = "main";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String state ;
        SharedPreferences sharedPreferences = getSharedPreferences("my_shared", MODE_PRIVATE);
        category = getIntent().getExtras().getString("category");
        if (category == null || "nothing".equals(category)){
            sharedPreferences.edit().putString("category","main").apply();
            category = "main";
        }
        Intent i2 = getIntent();
        state = i2.getStringExtra("state");
        String startingLetter = i2.getStringExtra("starting_word");
        tts = new TextToSpeech(this,i -> {
            if (i == TextToSpeech.SUCCESS) isSuccess = true;
        });
        conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        handler = new Handler();
        cronetApplication = new CronetApplication(this);
        cronetApplication.onCreate();
        executor = Executors.newSingleThreadExecutor();
        setContentView(R.layout.activity_main);
        roomDataBase = MyRoomDataBase.getInstance(this);
        if (state != null && !state.equals("search")) {
            words = roomDataBase.mainDataAccess().getWordsByCategory(category);
        }

        ImageButton ib = findViewById(R.id.click_button);
        ib.setOnClickListener(view1 -> addNewWord());
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
        adapter = new ProductAdapter(this, currentWord);
        Log.d("word_word", "onCreate: " + currentWord.size());
        recyclerView.setAdapter(adapter);
        goToSetting.setOnClickListener(this::onClick);
        input.setText(startingLetter);
        ProductAdapter.canDelete = true;
    }

    private void openSettingFragment() {
        ArrayList<String> options = new ArrayList<>();
        options.add("first");
        options.add("second");
        ListFragment fragment = new ListFragment(options);
        fragment.show(getSupportFragmentManager(),"no_tag");
    }

    private void updateWord(String input) {
        if (input == null) {
            Log.i("some error", "updateWord: some error!");
            Toast.makeText(this, "some error in updating word list", Toast.LENGTH_LONG).show();
            return;
        }
        currentWord.clear();
//        Log.d("word_word", "updateWord: " + category);
        for (Word word : words) {
            if (word.category != null && word.getWordItself().startsWith(input)) {
                currentWord.add(word);
            }
        }
        adapter.notifyDataSetChanged();// no other choice
        removed.clear();
    }

    private void addNewWord() {
        Log.i("tag tag", String.valueOf(currentWord.size()));
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
        boolean state = (netInfo == null);
        MyFragment myFragment = new MyFragment(this, !state);
        myFragment.show(getSupportFragmentManager(), "example");
    }


    private void openFragment(Word word) {
        MeaningFragment meaningFragment = new MeaningFragment(word.getWordItself(),
                word.getMeaning(), word.getNumber());
        meaningFragment.show(getSupportFragmentManager(), "my fragment");
    }

    public CronetApplication getCronetApplication() {
        return cronetApplication;
    }

    @Override
    public void ok_clicked(String word, String meaning) {
        Word newWord = new Word(word, meaning, 0, words.size(),this.category);
        if(!FunctionsStatic.internet_meaning.startsWith("a#"))
            newWord.setInternetMeaning(FunctionsStatic.internet_meaning);
        FunctionsStatic.internet_meaning = "a##";
        roomDataBase.mainDataAccess().insert(newWord);
        words.add(newWord);
        currentWord.add(newWord);
        adapter.notifyItemInserted(currentWord.size() - 1);
        if (this.category == null) {
            Toast.makeText(this, "some null category", Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public void starWord(String word) {
        for (Word word1 : words) {
            if (word1.getWordItself().equals(word)) {
                word1.increaseNumber();
                roomDataBase.mainDataAccess().update(word1.id, word1.wordItself, word1.meaning, word1.getNumber());
                break;
            }
        }
    }

    @Override
    public void deleteWord(int index, Word word) {
        int minus = 0;
        for (Integer i : removed) {
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
        for (Integer i : removed) {
            if (index >= i)
                minus++;
        }
        index -= minus;
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
        int minus = 0;
        for (Integer i : removed) {
            if (index >= i)
                minus++;
        }
        index -= minus;
        Word currentWord = words.get(index);
        currentViewWord = currentWord;
        Intent i = new Intent(MainActivity.this, MeaningActivity.class);
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
                        handler.post(() -> {
                           roomDataBase.mainDataAccess().update(currentViewWord.id,meaning);//saves unsaved stage
                        });
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
                    Toast.makeText(MainActivity.this,"some string",Toast.LENGTH_LONG).show();
                }
            };
            UrlRequest.Builder builderRequest = cronetApplication.getCronetEngine().
                    newUrlRequestBuilder(PATH_URL + currentWord.wordItself, mcb,
                            cronetApplication.getCronetCallbackExecutorService());
            builderRequest.build().start();
        } else if (currentWord.getInternetMeaning().startsWith("{\"title\":")) {
            openFragment(currentWord);
            Toast.makeText(MainActivity.this,"another string",Toast.LENGTH_LONG).show();
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
        return removed.size();
    }

    @Override
    public void btn_click_change(String newMeaning, Word word1, int index) {
        if (index == -1 || word1 == null) {
            index = words.size();
            String[] meaning = newMeaning.split("@");
            if (meaning.length >= 2) {
                word1 = new Word(meaning[0], meaning[1], 0, index,this.category);
                currentWord.add(word1);
                words.add(word1);
                adapter.notifyItemInserted(index);
            }
            if(! FunctionsStatic.internet_meaning.startsWith("a#") && word1 != null)
                word1.setInternetMeaning(FunctionsStatic.internet_meaning);
            roomDataBase.mainDataAccess().insert(word1);
            return;
        }
        word1.meaning = newMeaning;
        roomDataBase.mainDataAccess().update(word1.id, word1.wordItself, newMeaning, word1.getNumber());
        int minus = 0;
        for (Integer i : removed) {
            if (index >= i)
                minus++;
        }
        index -= minus;
        adapter.notifyItemChanged(index);
    }

    @Override
    public void addWord(String word, String meaning) {
        if (meaning == null || word == null) {
            Toast.makeText(this, "some error!", Toast.LENGTH_LONG).show();
        } else if (meaning.startsWith("something else")) {
            AddMeaningSomethingElse meaningSomethingElse = new AddMeaningSomethingElse(word);
            meaningSomethingElse.show(getSupportFragmentManager(), "no tag");
        } else
            ok_clicked(word, meaning);
    }

    @Override
    public void press(String input) {
    Toast.makeText(this,input,Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // To do
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