package com.example.myapplication.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.myapplication.R;

import java.util.ArrayList;

public class ListFragment extends DialogFragment {

    private ArrayList<String> strings;
    private DoTheAction action;

    public ListFragment(ArrayList<String> strings) {
        this.strings = strings;
        if (strings == null) {
            strings = new ArrayList<>();
        }
        if (strings.isEmpty()) {
            strings.add("nothing to show");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.list_fragment_view, null);
        LinearLayout layout = view.findViewById(R.id.view_of_list);
        for (String s : strings) {
            View theView = inflater.inflate(R.layout.text_in_menu, null);
            TextView text = theView.findViewById(R.id.changing_text);
            text.setText(s);
            theView.setOnClickListener(view1 -> {
                action.press(s);
                dismiss();
            });
            layout.addView(theView);
        }
        builder.setView(view);
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            action = (DoTheAction) context;
        } catch (ClassCastException c) {
            throw new ClassCastException("please implement the class first!");
        }
    }

    public interface DoTheAction {
        void press(String input);
    }
}
