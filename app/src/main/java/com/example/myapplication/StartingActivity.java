package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
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
import com.example.myapplication.fragments.AddCategory;
import com.example.myapplication.fragments.AddWordInStarting;
import com.example.myapplication.fragments.CategoryAdapter;
import com.example.myapplication.fragments.MyCategoryFragment;
import com.example.myapplication.fragments.MyFragment;
import com.example.myapplication.myword.Word;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class StartingActivity extends AppCompatActivity implements AddCategory.
        MyClickListenerAddCategory , CategoryAdapter.MyClickListener , AddWordInStarting.MyClickListener{
    LayoutInflater inflater;
    ArrayList<String> categories = new ArrayList<>();
    String categoryString = "";
    private Intent i;
    private List<Word> allWords;
    private MainDataAccess mainDataAccess;
    private CronetApplication cronetApplication;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starting);
        i = new Intent(StartingActivity.this,MainActivity.class);
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
                    i.putExtra("state", "search");
                    i.putExtra("starting_word", editable.toString());
                    startActivity(i);
                    editText.setText("");
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
        view.setOnClickListener(view1 -> addClicked());
        TextView textView = view.findViewById(R.id.description);
        textView.setText(R.string.add_word);
        layout.addView(view);
        view = inflater.inflate(R.layout.card_view_in_first_page, layout, false);
        view.setOnClickListener(view1 -> addCategory());
        textView = view.findViewById(R.id.description);
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



    private void addClicked() {
        boolean hasAccessToInternet = true; // needs to be implemented
        AddWordInStarting addWordInStarting = new AddWordInStarting(this, hasAccessToInternet, categoryString);
        addWordInStarting.show(getSupportFragmentManager(), "add word");
    }

    private void addCategory(){
        AddCategory addCategory = new AddCategory(categoryString);
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
        Word selectedWord = allWords.get(index);
        if (selectedWord.getInternetMeaning().startsWith("a#")){
            Toast.makeText(this,"No meaning is available for this word",Toast.LENGTH_LONG).show(); // temp
        } else {
            // temp TODO implement the function
        }
    }

    private void about(){
        Toast.makeText(this,"about",Toast.LENGTH_LONG).show();
    }

    @Override
    public void btn_click_add(String category) {
        if (categories.contains(category)) {
            Toast.makeText(this, "Category already exists", Toast.LENGTH_LONG).show();
        } else {
            categoryString = "";
            for (String s : categories) {
                categoryString += (s + "#");
            }
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
        startActivity(i);
    }

    @Override
    public void ok_clicked(String word, String meaning, String category) {
        Word word1 = new Word(word,meaning,0,0);
        for (Word word2 : allWords){
            if (word2.getWordItself().equals(word)){
                Toast.makeText(this,"Word already exists",Toast.LENGTH_LONG).show();
                return;
            }
        }
        mainDataAccess.insert(word1);
        allWords.add(word1);
        Toast.makeText(this,"Word " + word1.getWordItself().toString() +
                " added successfully",Toast.LENGTH_LONG).show();
    }

    public CronetApplication getCronetApplication() {
        return cronetApplication;
    }


}