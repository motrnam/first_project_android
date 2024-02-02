package com.example.myapplication;

import android.content.Intent;
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

import com.example.myapplication.Database.MyRoomDataBase;

public class StartingActivity extends AppCompatActivity {
    LayoutInflater inflater;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starting);
        Intent i = new Intent(StartingActivity.this,MainActivity.class);
        MyRoomDataBase.getInstance(this);
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
                    i.putExtra("starting_word", editable.toString());
                    startActivity(i);
                    editText.setText("");
                }
            }
        });
        LinearLayout add = findViewById(R.id.linearLayoutStart);
        addButtons(add);
    }

    private void addButtons(LinearLayout layout) {
        inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.card_view_in_first_page, layout, false);
        view.setOnClickListener(view1 -> addClicked());
        TextView textView = view.findViewById(R.id.desciption);
        textView.setText("Add word");
        layout.addView(view);
        view = inflater.inflate(R.layout.card_view_in_first_page, layout, false);
        view.setOnClickListener(view1 -> addCategory());
        textView = view.findViewById(R.id.desciption);
        textView.setText("Add Category");
        layout.addView(view);
        view = inflater.inflate(R.layout.card_view_in_first_page, layout, false);
        view.setOnClickListener(view1 -> myCategory());
        textView = view.findViewById(R.id.desciption);
        textView.setText("My C");
        layout.addView(view);
        view = inflater.inflate(R.layout.card_view_in_first_page, layout, false);
        view.setOnClickListener(view1 -> pickRandom());
        textView = view.findViewById(R.id.desciption);
        textView.setText("Random");
        layout.addView(view);
        view = inflater.inflate(R.layout.card_view_in_first_page, layout, false);
        view.setOnClickListener(view1 -> about());
        textView = view.findViewById(R.id.desciption);
        textView.setText("About");
        layout.addView(view);
    }



    private void addClicked() {
        Toast.makeText(this,"add word is clicked!",Toast.LENGTH_LONG).show();
    }

    private void clickCards(){
        Toast.makeText(this,"add word is clicked!",Toast.LENGTH_LONG).show();
    }

    private void addCategory(){
        Toast.makeText(this,"add word is clicked!",Toast.LENGTH_LONG).show();
    }

    private void myCategory(){
        Toast.makeText(this,"my category",Toast.LENGTH_LONG).show();
    }

    private void pickRandom(){
        Toast.makeText(this,"pick word ",Toast.LENGTH_LONG).show();
    }

    private void about(){
        Toast.makeText(this,"about",Toast.LENGTH_LONG).show();
    }

}