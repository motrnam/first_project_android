package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;

import com.example.myapplication.Database.MyRoomDataBase;

public class StartingActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starting);
        Button temp = findViewById(R.id.button);
        Intent i = new Intent(StartingActivity.this,MainActivity.class);
        temp.setOnClickListener(view -> {
            startActivity(i);
        });
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
    }
}