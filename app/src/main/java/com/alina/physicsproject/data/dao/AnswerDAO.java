package com.alina.physicsproject.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.alina.physicsproject.data.object.AnswerTable;
import com.alina.physicsproject.data.object.SignTable;

import java.util.List;

@Dao
public interface AnswerDAO {

    @Query("SELECT * FROM answer")
    LiveData<List<AnswerTable>> getAllAnswer();

    @Insert
    void insertAnswer(AnswerTable answerTable);

    @Delete
    void deleteAnswer(AnswerTable answerTable);

    @Query("DELETE FROM answer")
    void deletedAllAnswer();
}
