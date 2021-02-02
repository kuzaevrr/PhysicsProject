package com.alina.physicsproject.data.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.alina.physicsproject.data.dao.AnswerDAO;
import com.alina.physicsproject.data.dao.SignDAO;
import com.alina.physicsproject.data.object.AnswerTable;


@Database(entities = {AnswerTable.class}, version = 1, exportSchema = false)
public abstract class AnswerDatabase extends RoomDatabase {

    private static AnswerDatabase database;
    private static final String DB_NAME = "answer.db";
    private static final Object LOCK = new Object();

    public static AnswerDatabase getInstance(Context context){
        synchronized (LOCK){
            if (database == null){
                database = Room.databaseBuilder(context, AnswerDatabase.class, DB_NAME)
                        .build();
            }
        }
        return database;
    }

    public abstract AnswerDAO answerDAO();
}
