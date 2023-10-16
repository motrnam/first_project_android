package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.stream.JsonReader;

import org.jetbrains.annotations.Contract;

import java.io.StringReader;


public class FunctionsStatic {
    public static String internet_meaning = "a##";
    @Nullable
    public static FinalWordFromInternet getFinalByString(@NonNull String input){
        if (input.startsWith("{\"title\":\"No"))
            return null;
        Gson gson = new Gson();
        input = "{ all: " + input + "}";
        input = input.trim();
        JsonReader reader = new JsonReader(new StringReader(input));
        reader.setLenient(true);
        return gson.fromJson(reader,FinalWordFromInternet.class);
    }
}
