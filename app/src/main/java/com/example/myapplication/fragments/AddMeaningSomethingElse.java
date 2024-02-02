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

public class AddMeaningSomethingElse extends DialogFragment {
    private final String word;
    ChangeMeaningFragment.MyClickListener2 myClickListener2;
    public AddMeaningSomethingElse(String word){
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
        meaningEdit.setHint("meaning");
        TextView tv = view.findViewById(R.id.word_string);
        tv.setText(this.word);
        okButton.setOnClickListener(view1 -> {
            myClickListener2.btn_click_change(word + "@" +
                    meaningEdit.getText().toString(),null,-1);
            dismiss();
        });
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            myClickListener2 = (ChangeMeaningFragment.MyClickListener2) context;
        }catch (ClassCastException castException){
            throw new ClassCastException("please implement the required class!");
        }
    }
}
