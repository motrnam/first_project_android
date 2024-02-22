package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.Database.MainDataAccess;
import com.example.myapplication.Database.MyRoomDataBase;
import com.example.myapplication.fragments.AboutFragment;
import com.example.myapplication.fragments.AddCategory;
import com.example.myapplication.fragments.AskMeaningFragment;
import com.example.myapplication.fragments.CategoryAdapter;
import com.example.myapplication.fragments.MyCategoryFragment;
import com.example.myapplication.myword.Word;

import org.chromium.net.UrlRequest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class StartingActivity extends AppCompatActivity implements AddCategory.
        MyClickListenerAddCategory , CategoryAdapter.MyClickListener {
    LayoutInflater inflater;
    ArrayList<String> categories = new ArrayList<>();
    String categoryString = "";
    private Intent i,i2;
    private List<Word> allWords;
    public final static String PATH_URL = "https://api.dictionaryapi.dev/api/v2/entries/en/";
    private MainDataAccess mainDataAccess;
    private CronetApplication cronetApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starting);
        i = new Intent(StartingActivity.this,MainActivity.class);
        i2 = new Intent(StartingActivity.this,Search2Activity.class);
        mainDataAccess = MyRoomDataBase.getInstance(this).mainDataAccess();
        allWords = mainDataAccess.getAll();
        EditText editText = findViewById(R.id.editTextText);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!editable.toString().equals("")) {
                    i2.putExtra("state", "search");
                    i2.putExtra("starting_word", editable.toString());
                    editText.setText("");
                    startActivity(i2);
                }
            }
        });
        LinearLayout add = findViewById(R.id.linearLayoutStart);
        addButtons(add);
        SharedPreferences sharedPreferences = getSharedPreferences("my_shared", MODE_PRIVATE);
        categoryString = sharedPreferences.getString("category", "main");
        String[] categoryArray = categoryString.split("#");
        Collections.addAll(categories, categoryArray);
        cronetApplication = new CronetApplication(this);
        cronetApplication.onCreate();
    }

    private void addButtons(LinearLayout layout) {
        inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.card_view_in_first_page, layout, false);
        view.setOnClickListener(view1 -> addCategory());
        TextView textView = view.findViewById(R.id.description);
        textView.setText(R.string.add_category);
        layout.addView(view);
        view = inflater.inflate(R.layout.card_view_in_first_page, layout, false);
        view.setOnClickListener(view1 -> myCategory());
        textView = view.findViewById(R.id.description);
        textView.setText(R.string.my_categories);
        layout.addView(view);
        view = inflater.inflate(R.layout.card_view_in_first_page, layout, false);
        view.setOnClickListener(view1 -> pickRandom());
        textView = view.findViewById(R.id.description);
        textView.setText(R.string.pick_randomly);
        layout.addView(view);
        view = inflater.inflate(R.layout.card_view_in_first_page, layout, false);
        view.setOnClickListener(view1 -> about());
        textView = view.findViewById(R.id.description);
        textView.setText(R.string.about);
        layout.addView(view);
    }



    private void addCategory(){
        AddCategory addCategory = new AddCategory();
        addCategory.show(getSupportFragmentManager(),"add category");
    }

    private void myCategory(){
        //Toast.makeText(this,categoryString,Toast.LENGTH_LONG).show();
        MyCategoryFragment myCategoryFragment = new MyCategoryFragment(categoryString ,this);
        myCategoryFragment.show(getSupportFragmentManager(),"my category");
    }

    private void pickRandom(){ // it needs to be implemented (some of it is implemented)
        Random random = new Random();
        if (allWords.isEmpty()){
            Toast.makeText(this,"No word is available",Toast.LENGTH_LONG).show();
            return;
        }
        int index = random.nextInt(allWords.size());
        Word currentWord = allWords.get(index);
        if (currentWord.getInternetMeaning().startsWith("a#")){
            Toast.makeText(this,"No meaning is available for this word",Toast.LENGTH_LONG).show(); // temp
        } else {
            i = new Intent(StartingActivity.this, MeaningActivity.class);
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
                        Toast.makeText(StartingActivity.this,"some string",Toast.LENGTH_LONG).show();
                    }
                };
                UrlRequest.Builder builderRequest = cronetApplication.getCronetEngine().
                        newUrlRequestBuilder(PATH_URL + currentWord.wordItself, mcb,
                                cronetApplication.getCronetCallbackExecutorService());
                builderRequest.build().start();
            } else if (currentWord.getInternetMeaning().startsWith("{\"title\":")) {
                openFragment(currentWord);
                Toast.makeText(StartingActivity
                        .this,"another string",Toast.LENGTH_LONG).show();
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
    }

    private void openFragment(Word currentWord) {
        List<String> stringList = new ArrayList<>();
        stringList.add("No meaning is available for this word");
        AskMeaningFragment theFragment = new AskMeaningFragment(stringList,
                currentWord.getWordItself(), this);
        theFragment.show(getSupportFragmentManager(), "this tag");
    }

    private void about(){
        AboutFragment aboutFragment = new AboutFragment();
        aboutFragment.show(getSupportFragmentManager(),"about");
    }

    @Override
    protected void onResume() {
        super.onResume();
        allWords = mainDataAccess.getAll();
    }

    @Override
    public void btn_click_add(String category) {
        if (categories.contains(category)) {
            Toast.makeText(this, "Category already exists", Toast.LENGTH_LONG).show();
        } else {
            categoryString = "";
            StringBuilder stringBuilder = new StringBuilder();
            for (String s : categories) stringBuilder.append(s).append("#");
            categoryString = stringBuilder.toString();
            categories.add(category);
            categoryString += category;
            SharedPreferences sharedPreferences = getSharedPreferences("my_shared", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("category", categoryString);
            editor.apply();
            Toast.makeText(this, "Category added successfully", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void btn_click(String category) {
        i = new Intent(StartingActivity.this,MainActivity.class);
        i.putExtra("state","category");
        i.putExtra("category",category);
        i.putExtra("current_category",category);
        i.putExtra("secret",category);
        startActivity(i);
    }

}