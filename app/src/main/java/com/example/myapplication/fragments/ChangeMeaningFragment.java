package com.example.myapplication.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.myapplication.R;
import com.example.myapplication.myword.Word;

public class ChangeMeaningFragment extends DialogFragment {
    private String wordString,meaningString;
    private MyClickListener2 myClickListener2;
    private final Word word;
    public ChangeMeaningFragment(String wordString, String meaningString,Word word) {
        this.wordString = wordString;
        this.meaningString = meaningString;
        this.word = word;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.change_meaning_word,null);
        Button okButton = view.findViewById(R.id.ok_button);
        EditText meaningEdit = view.findViewById(R.id.meaning);
        TextView tv = view.findViewById(R.id.word_string);
        tv.setText(this.wordString);
        meaningEdit.setText(this.meaningString);
        okButton.setText(R.string.change);
        okButton.setOnClickListener(view1 -> {
            myClickListener2.btn_click_change(meaningEdit.getText().toString(),word,0);
            dismiss();
        });
        builder.setView(view);
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            myClickListener2 = (MyClickListener2) context;
        }catch (ClassCastException castException){
            throw new ClassCastException("you should implement this interface!");
        }
    }

    public interface MyClickListener2{
        void btn_click_change(String newMeaning,Word word1,int index);
    }
}
