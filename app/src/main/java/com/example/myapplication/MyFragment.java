package com.example.myapplication;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class MyFragment extends DialogFragment {
    private MyClickListener myClickListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.add_word_layout, null);
        builder.setView(view);
        Button buttonOk = view.findViewById(R.id.ok_button);
        EditText wordEdit = view.findViewById(R.id.word);
        EditText meaningEdit = view.findViewById(R.id.meaning);
        buttonOk.setOnClickListener(view1 -> {
            myClickListener.ok_clicked(wordEdit.getText().toString(),meaningEdit.getText().toString());
            dismiss();
        });
        Button cancel = view.findViewById(R.id.cancel_button);
        cancel.setOnClickListener(view1 -> dismiss());
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            myClickListener = (MyClickListener) context;
        }catch (ClassCastException e){
            throw new ClassCastException("you should implement listener in your in activity");
        }
    }

    public interface MyClickListener{
        void ok_clicked(String word,String meaning);
    }
}
