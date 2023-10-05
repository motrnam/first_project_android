package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Database.MyRoomDataBase;

import org.chromium.net.UrlRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity implements MyFragment.MyClickListener,
        MeaningFragment.ListenerOfClas, ProductAdapter.ClickListener,
        ChangeMeaningFragment.MyClickListener2, AskMeaningFragment.TheListener,
        ListFragment.DoTheAction {
    private RecyclerView recyclerView;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        handler = new Handler();
        cronetApplication = new CronetApplication(this);
        cronetApplication.onCreate();
        executor = Executors.newSingleThreadExecutor();
        setContentView(R.layout.activity_main);
        roomDataBase = MyRoomDataBase.getInstance(this);
        words = roomDataBase.mainDataAccess().getAll();
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
        recyclerView = findViewById(R.id.rec);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ProductAdapter(this, currentWord);
        recyclerView.setAdapter(adapter);
        goToSetting.setOnClickListener(view -> {
            openSettingFragment();
        });
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
            Toast.makeText(this, "some error", Toast.LENGTH_LONG).show();
            return;
        }
        currentWord.clear();
        for (Word word : words) {
            if (word.getWordItself().startsWith(input))
                currentWord.add(word);
            if (currentWord.size() >= 100)
                break;
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
                word.getMeaning(), word.getNumber(), word.getIndex());
        meaningFragment.show(getSupportFragmentManager(), "my fragment");
    }

    public CronetApplication getCronetApplication() {
        return cronetApplication;
    }

    @Override
    public void ok_clicked(String word, String meaning) {
        Word newWord = new Word(word, meaning, 0, words.size());
        roomDataBase.mainDataAccess().insert(newWord);
        words.add(newWord);
        currentWord.add(newWord);
        adapter.notifyItemInserted(currentWord.size() - 1);
    }

    @Override
    public String getMeaningFromInternet(String word, MyFragment myFragment) {
        return null;
    }

    @Override
    public Handler getHandler() {
        return handler;
    }

    @Override
    public void starWord(int indexOF) {
        if (indexOF < words.size()) {
            Word w = words.get(indexOF);
            w.increaseNumber();
            roomDataBase.mainDataAccess().update(w.id, w.wordItself, w.getMeaning(), getNumber());
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
        ChangeMeaningFragment changeMeaningFragment = new ChangeMeaningFragment(words.
                get(index).wordItself, words.get(index).meaning, words.get(index));
        changeMeaningFragment.show(getSupportFragmentManager(), "my bag");
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
        Intent i = new Intent(MainActivity.this, MeaningActivity.class);
        MyCallBack mcb = new MyCallBack() {
            @Override
            public void onSucceeded(String meaning) {
                if (meaning.startsWith("{\"title\":\"No Definitions Found\"")) {
                    openFragment(currentWord);
                } else {
                    i.putExtra("word", meaning);
                    i.putExtra("meaning", currentWord.getMeaning());
                    startActivity(i);
                }
            }

            @Override
            public void onFailed() {
                openFragment(currentWord);
            }
        };
        UrlRequest.Builder builderRequest = cronetApplication.getCronetEngine().
                newUrlRequestBuilder(PATH_URL + currentWord.wordItself, mcb,
                        cronetApplication.getCronetCallbackExecutorService());
        builderRequest.build().start();
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
                word1 = new Word(meaning[0], meaning[1], 0, index);
                currentWord.add(word1);
                words.add(word1);
                adapter.notifyItemInserted(index);
            }
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
        } else if (meaning.startsWith("something els")) {
            AddMeaningSomethingElse meaningSomethingElse = new AddMeaningSomethingElse(word);
            meaningSomethingElse.show(getSupportFragmentManager(), "no tag");
        } else
            ok_clicked(word, meaning);
    }

    @Override
    public void press(String input) {
        Toast.makeText(this,input,Toast.LENGTH_LONG).show();
    }
}