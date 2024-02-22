package com.example.myapplication.fragments;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.myapplication.R;

public class MeaningFragment extends DialogFragment {
    private final String word;
    private final String meaning;
    private final int number;
    private ListenerOfClas listenerOfClass;

    public MeaningFragment(String word, String meaning,int number){
        this.word = word;
        this.meaning = meaning;
        this.number = number;
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listenerOfClass = (ListenerOfClas) context;
        }catch (ClassCastException castException){
            throw new ClassCastException("you should implement " + this.getClass().getName());
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.check_word_layout,null);
        TextView wordTextView = view.findViewById(R.id.word_in_cwl);
        TextView meaningView = view.findViewById(R.id.meaning_in_cwl);
        wordTextView.setText(this.word);
        meaningView.setText(this.meaning);
        ProgressBar pb = view.findViewById(R.id.progressBar);
        pb.setProgress(number * 100 / 7);
        Button buttonAdd = view.findViewById(R.id.learnBtn);
        buttonAdd.setOnClickListener(view1 -> {
            listenerOfClass.starWord(word);
            MeaningFragment.this.dismiss();
        });
        builder.setView(view);
        return builder.create();
    }

    public interface ListenerOfClas{
        void starWord(String word);
    }
}
