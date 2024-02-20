package com.example.myapplication.myword;

import android.util.Log;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "word_table")
public class Word implements Comparable<Word> {
    public String getWordItself() {
        return wordItself;
    }

    public String getMeaning() {
        return meaning;
    }

    public void setInternetMeaning(String internetMeaning) {
        this.internetMeaning = internetMeaning; // save
    }

    public int getNumber() {
        return number;
    }
    @ColumnInfo(name = "word_string")
    public final String wordItself;
    @ColumnInfo(name = "meaning")
    public String meaning;
    @ColumnInfo(name = "category")
    public String category;
    @PrimaryKey(autoGenerate = true)
    public int id = 0;
    private final int index;
    @ColumnInfo(name = "internet_meaning")
    public String internetMeaning = "nothing";
    @ColumnInfo(name = "number_of_review")
    private int number;

    public String getInternetMeaning() {
        return internetMeaning;
    }

    public Word(String wordItself, String meaning, int number, int index,String category) {
        this.wordItself = wordItself;
        this.meaning = meaning;
        this.number = number;
        this.index = index;
        this.category = category;
        if (category == null){
            this.category = "main";
            Log.d("word_word", "Word: "+ wordItself+" has no category");
        }else {
            this.category = category;
            Log.d("word_word", "Word:" + wordItself + " has category " + category);
        }
    }

    public int getIndex() {
        return index;
    }

    public void increaseNumber() {
        if (number < 7)
            number++;
    }

    @Override
    public int compareTo(Word word) {
        if (word.wordItself.equals(this.wordItself))
            return 0;
        return this.wordItself.compareTo(word.wordItself);
    }

    public String getCategory() {
        return category;
    }
}
