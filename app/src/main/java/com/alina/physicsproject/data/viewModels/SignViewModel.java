package com.alina.physicsproject.data.viewModels;

import android.app.Application;
import android.app.AsyncNotedAppOp;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.alina.physicsproject.data.database.SignDatabase;
import com.alina.physicsproject.data.object.SignTable;

import java.util.List;

public class SignViewModel extends AndroidViewModel {

    private static SignDatabase database;
    private LiveData<List<SignTable>> listLiveData;

    public SignViewModel(@NonNull Application application) {
        super(application);
        database = SignDatabase.getInstance(getApplication());
        listLiveData = database.signDAO().getAllMail();
    }

    public LiveData<List<SignTable>> getListLiveData() {
        return listLiveData;
    }

    public void insertSign(SignTable signTable){
        new InsertTask().execute(signTable);
    }

    public void deleteSign(SignTable signTable){
        new DeleteTask().execute(signTable);
    }

    public void deleteAllSign(){
        new DeleteAllTask().execute();
    }

    private static class InsertTask extends AsyncTask<SignTable, Void, Void>{

        @Override
        protected Void doInBackground(SignTable... signTables) {
            if(signTables != null || signTables.length >0){
                database.signDAO().insertMail(signTables[0]);
            }
            return null;
        }
    }

    private static class DeleteTask extends AsyncTask<SignTable, Void, Void>{

        @Override
        protected Void doInBackground(SignTable... signTables) {
            if(signTables != null || signTables.length >0){
                database.signDAO().deleteMail(signTables[0]);
            }
            return null;
        }
    }

    private static class DeleteAllTask extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void ... signTables) {
                database.signDAO().deletedAllMail();
            return null;
        }
    }
}
