package com.example.myapplication.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.myapplication.CronetApplication;
import com.example.myapplication.FunctionsStatic;
import com.example.myapplication.MyCallBack;
import com.example.myapplication.R;
import com.example.myapplication.StartingActivity;
import com.example.myapplication.myword.WordDefinition;
import com.example.myapplication.myword.WordFromInternet;
import com.example.myapplication.myword.WordMeaning;
import com.google.android.material.button.MaterialButtonToggleGroup;

import org.chromium.net.UrlRequest;

import java.util.ArrayList;
import java.util.List;

public class AddWordInStarting extends DialogFragment {
    public final static String PATH_URL = "https://api.dictionaryapi.dev/api/v2/entries/en/";
    private final StartingActivity mainActivity;
    private final boolean hasAccessToInternet;
    private MyClickListener myClickListener;
    private String selectedCategory;
    private String[] categories = {"main"};
    private MyCallBack mcb;
    public AddWordInStarting(StartingActivity activity, boolean hasAccessToInternet, String category) {
        this.mainActivity = activity;
        this.hasAccessToInternet = hasAccessToInternet;
        if (category != null)
            categories = category.split("#");
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
//        Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> );
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.add_word_layout_first, null);
        builder.setView(view);
        Button buttonOk = view.findViewById(R.id.ok_button);
        EditText wordEdit = view.findViewById(R.id.word);
        EditText meaningEdit = view.findViewById(R.id.meaning);
        ImageView ib = view.findViewById(R.id.get_from_internet);
        mcb = new MyCallBack() {
            @Override
            public void onSucceeded(String meaning) {
                if (meaning == null || meaning.startsWith("g")) {
                    Toast.makeText(getContext(), "no meaning in the internet", Toast.LENGTH_LONG)
                            .show();
                    return;
                }
                try {
                    List<WordFromInternet> fwfi = (FunctionsStatic.getFinalByString(meaning)).all;
                    List<WordDefinition> definitions = new ArrayList<>();
                    List<String> stringList = new ArrayList<>();
                    for (WordFromInternet word : fwfi) {
                        for (WordMeaning wordMeaning : word.meanings)
                            definitions.addAll(wordMeaning.definitions);
                    }
                    for (WordDefinition definition : definitions) {
                        stringList.add(definition.definition);
                    }
                    AskMeaningFragment theFragment = new AskMeaningFragment(stringList,
                            wordEdit.getText().toString(), mainActivity);
                    theFragment.show(mainActivity.getSupportFragmentManager(), "this tag");
                    FunctionsStatic.internet_meaning = meaning;
                    dismiss();
                } catch (Exception e) {
                    Toast.makeText(getContext(), "some problem", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailed() {
                Toast.makeText(getContext(), "some problem caused!", Toast.LENGTH_LONG).show();
            }
        };

        ib.setOnClickListener(view1 -> {
            if (hasAccessToInternet) {
                CronetApplication cronetApplication = mainActivity.getCronetApplication();
                UrlRequest.Builder builderRequest = cronetApplication.getCronetEngine().
                        newUrlRequestBuilder(PATH_URL + wordEdit.getText().toString(), mcb,
                                cronetApplication.getCronetCallbackExecutorService());
                builderRequest.build().start();
            } else
                Toast.makeText(mainActivity, "no Internet connection", Toast.LENGTH_LONG).show();
        });


        buttonOk.setOnClickListener(view1 -> {
            myClickListener.ok_clicked(wordEdit.getText().toString(), meaningEdit.getText().
                    toString(), "main");
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
        } catch (ClassCastException e) {
            throw new ClassCastException("you should implement listener in your in activity");
        }
    }

    public interface MyClickListener {
        void ok_clicked(String word, String meaning, String category);
    }
}
