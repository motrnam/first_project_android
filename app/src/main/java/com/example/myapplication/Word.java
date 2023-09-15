package com.example.myapplication;

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

    public int getNumber() {
        return number;
    }
    @ColumnInfo(name = "word_string")
    public final String wordItself;
    @ColumnInfo(name = "meaning")
    public String meaning;
    @PrimaryKey(autoGenerate = true)
    public int id = 0;
    private final int index;
    @ColumnInfo(name = "number_of_review")
    private int number;

    public Word(String wordItself, String meaning, int number, int index) {
        this.wordItself = wordItself;
        this.meaning = meaning;
        this.number = number;
        this.index = index;
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
}
