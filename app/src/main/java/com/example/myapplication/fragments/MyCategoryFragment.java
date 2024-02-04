package com.example.myapplication.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

public class MyCategoryFragment extends DialogFragment {
    private final String[] categories_array;
    private final Context context;
    public MyCategoryFragment(String categories,Context context){
        categories_array = categories.split("#");
        this.context = context;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        //builder.setTitle("Choose a category");
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.my_category, null);
        RecyclerView recyclerView = view.findViewById(R.id.myRecyclerView);
        recyclerView.setLayoutManager(new androidx.recyclerview.widget.LinearLayoutManager(context));
        CategoryAdapter adapter = new CategoryAdapter(categories_array, context,this);
        recyclerView.setAdapter(adapter);
        builder.setView(view);
        return builder.create();
    }
}
