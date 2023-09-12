package com.example.myapplication.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.myapplication.Word;

@Database(entities = Word.class,version = 1,exportSchema = false)
public abstract class MyRoomDataBase extends RoomDatabase {
    private static MyRoomDataBase room;
    private static String DATABASE_NAME = "word_database";
    public synchronized static MyRoomDataBase getInstance(Context context){
        if (room == null){
            room = Room.databaseBuilder(context.getApplicationContext(), MyRoomDataBase.class,
                    DATABASE_NAME).
                    allowMainThreadQueries().
                    fallbackToDestructiveMigration().
                    build();
        }
        return room;
    }

    public abstract MainDataAccess mainDataAccess();
}
