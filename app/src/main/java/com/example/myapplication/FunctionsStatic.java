package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.stream.JsonReader;

import org.jetbrains.annotations.Contract;

import java.io.StringReader;


public class FunctionsStatic {

    public static String getMainMeaning(@NonNull String input) {
        if (input.startsWith("{\"title\":\"No"))
            return "No such word found";
        Gson gson = new Gson();
        try {
            input = "{ all: " + input + "}";
            input = input.trim();
            JsonReader reader = new JsonReader(new StringReader(input));
            reader.setLenient(true);
            FinalWordFromInternet fwfi = gson.fromJson(reader,FinalWordFromInternet.class);
            return fwfi.all.get(0).meanings.get(0).definitions.get(0).definition;
        } catch (ClassCastException castException) {
            return "we encounter classCastException please contact me at motrnam@gmail.com";
        }catch (IndexOutOfBoundsException e){
            return "IndexOutOfBoundsException please contact me at motrnam@gmail.com";
        } catch (JsonParseException exception){
            return "A problem has been caused";
        }
    }

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
