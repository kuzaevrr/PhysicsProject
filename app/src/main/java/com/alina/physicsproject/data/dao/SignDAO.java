package com.alina.physicsproject.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.alina.physicsproject.data.object.SignTable;

import java.util.List;

@Dao
public interface SignDAO {

    @Query("SELECT * FROM sign")
    LiveData<List<SignTable>> getAllMail();

    @Insert
    void insertMail(SignTable MailTable);

    @Delete
    void deleteMail(SignTable MailTable);

    @Query("DELETE FROM sign")
    void deletedAllMail();

}
