package com.alina.physicsproject.data.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.alina.physicsproject.data.dao.SignDAO;
import com.alina.physicsproject.data.object.SignTable;

@Database(entities = {SignTable.class}, version = 1, exportSchema = false)
public abstract class SignDatabase extends RoomDatabase {

    private static SignDatabase database;
    private static final String DB_NAME = "sign.db";
    private static final Object LOCK = new Object();

    public static SignDatabase getInstance(Context context){
        synchronized (LOCK){
            if (database == null){
                database = Room.databaseBuilder(context, SignDatabase.class, DB_NAME)
                        .build();
            }
        }
        return database;
    }

    public abstract SignDAO signDAO();
}
