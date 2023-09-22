package com.example.myapplication;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import org.jetbrains.annotations.Contract;


public class FunctionsStatic {

    public static String getMainMeaning(String input) {
        if (input.startsWith("{\"title\":\"No"))
            return "No such word found";
        Gson gson = new Gson();
        try {
            input = "{ all: " + input + "}";
            FinalWordFromInternet fwfi = gson.fromJson(input,FinalWordFromInternet.class);
            return fwfi.all.get(0).meanings.get(0).definitions.get(0).definition;
        } catch (ClassCastException castException) {
            return "we encounter classCastException please contact me at motrnam@gmail.com";
        }catch (IndexOutOfBoundsException e){
            return "IndexOutOfBoundsException please contact me at motrnam@gmail.com";
        }
//        Pattern pattern = Pattern.compile(ALL_MEANING_REGEX_PATTERN);
//        Matcher matcher = pattern.matcher(input);
//        if (matcher.matches()){
//            return matcher.group("def");
//        }
    }

    @NonNull
    @Contract(pure = true)
    public static String getAllMeaning(String input) {
        return "";
    }
}
