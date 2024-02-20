package com.example.myapplication.Database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import static androidx.room.OnConflictStrategy.REPLACE;

import com.example.myapplication.myword.Word;

import java.util.List;

@Dao
public interface MainDataAccess {
    @Insert(onConflict = REPLACE)
    void insert(Word word);

    @Query("SELECT * FROM word_table ORDER BY id DESC")
    List<Word> getAll();
    @Query("SELECT * FROM word_table WHERE category = :category ORDER BY id DESC")
    List<Word> getWordsByCategory(String category);
    @Query("UPDATE word_table SET word_string = :wordString, meaning = :meaning, number_of_review = :number WHERE ID = :id")
    void update(int id,String wordString,String meaning,int number);

    @Query("UPDATE word_table SET internet_meaning = :internetMeaning WHERE ID = :id")
    void update(int id,String internetMeaning);

    @Query("UPDATE word_table SET number_of_review = :number WHERE ID = :id")
    void update(int id,int number);

    @Delete
    void delete(Word word);
}
