package com.alina.physicsproject.data.viewModels;

import android.app.Application;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.alina.physicsproject.data.database.AnswerDatabase;
import com.alina.physicsproject.data.object.AnswerTable;

import java.util.List;

public class AnswerViewModel extends AndroidViewModel {

    private static AnswerDatabase database;
    private LiveData<List<AnswerTable>> listLiveData;

    public AnswerViewModel(@NonNull Application application) {
        super(application);
        database = AnswerDatabase.getInstance(getApplication());
        listLiveData = database.answerDAO().getAllAnswer();
    }

    public LiveData<List<AnswerTable>> getListLiveData() {
        return listLiveData;
    }

    public void insertAnswer(AnswerTable answerTable){
        new AnswerViewModel.InsertTask().execute(answerTable);
    }

    public void deleteAnswer(AnswerTable answerTable){
        new AnswerViewModel.DeleteTask().execute(answerTable);
    }

    public void deleteAllAnswer(){
        new AnswerViewModel.DeleteAllTask().execute();
    }

    private static class InsertTask extends AsyncTask<AnswerTable, Void, Void> {

        @Override
        protected Void doInBackground(AnswerTable... answerTables) {
            if(answerTables != null || answerTables.length >0){
                database.answerDAO().insertAnswer(answerTables[0]);
            }
            return null;
        }
    }

    private static class DeleteTask extends AsyncTask<AnswerTable, Void, Void>{

        @Override
        protected Void doInBackground(AnswerTable... answerTables) {
            if(answerTables != null || answerTables.length >0){
                database.answerDAO().deleteAnswer(answerTables[0]);
            }
            return null;
        }
    }

    private static class DeleteAllTask extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void ... signTables) {
            database.answerDAO().deletedAllAnswer();
            return null;
        }
    }
}
