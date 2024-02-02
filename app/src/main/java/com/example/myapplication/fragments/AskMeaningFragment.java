package com.example.myapplication.fragments;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.myapplication.R;

import java.util.List;

public class AskMeaningFragment extends DialogFragment {
    List<String> allItems;
    String word;
    private TheListener listener;
    private final Context context;
    public AskMeaningFragment(List<String> allItems, String word, Context context){
        this.allItems = allItems;
        allItems.add("something else!");
        this.word = word;
        this.context = context;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (TheListener) context;
        }catch (ClassCastException castException){
            throw new ClassCastException("you should implement this class!" + context.getClass().
                    getName());
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.for_new_fragment,null);
        LinearLayout layout = view.findViewById(R.id.add_meaning_layout);
        for (String item:allItems){
            View view1 = inflater.inflate(R.layout.show_meaning,null);
            TextView textView = view1.findViewById(R.id.meaning_textview_show);
            textView.setText(item);
            layout.addView(view1);
            view1.setOnClickListener(view2 -> {
                listener.addWord(word,item);
                dismiss();
            });
        }
        builder.setView(view);
        return builder.create();
    }

    public interface TheListener{
        void addWord(String word,String meaning);
    }

}
