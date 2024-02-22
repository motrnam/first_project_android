package com.example.myapplication.fragments;

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

import com.example.myapplication.R;

public class AddCategory extends DialogFragment {
    private MyClickListenerAddCategory myClickListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        builder.setTitle("Choose a category");
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.add_category,null);
        EditText editText = view.findViewById(R.id.category_edit_text);
        Button button = view.findViewById(R.id.add_category_button);
        button.setOnClickListener(view1 -> {
            myClickListener.btn_click_add(editText.getText().toString());
            dismiss();
        });
        builder.setView(view);
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            myClickListener = (MyClickListenerAddCategory) context;
        } catch (ClassCastException castException) {
            throw new ClassCastException("please implement the required class! "
                    + context.getClass().getName());
        }
    }

    public interface MyClickListenerAddCategory {
        void btn_click_add(String category);
    }
}
