package com.alina.physicsproject.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alina.physicsproject.R;
import com.alina.physicsproject.data.object.AnswerTable;
import com.alina.physicsproject.data.viewModels.AnswerViewModel;

import java.util.List;

public class Lk extends AppCompatActivity {

    private AnswerViewModel answerViewModel;
    private LinearLayout llDBRES;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lk);

        llDBRES = (LinearLayout) findViewById(R.id.llDBRES);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        answerViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(AnswerViewModel.class);
//        DBHelper dbHelper = new DBHelper(this); // объект DBHELPER обращение к БД
//        SQLiteDatabase database = dbHelper.getWritableDatabase(); //Создание объекта БД
//        Cursor myCursor = database.query(DBHelper.Table_Answer, //таблица в БД
//                null, null, null, null, null, null);

        LiveData<List<AnswerTable>> listLiveData = answerViewModel.getListLiveData();
            listLiveData.observe(this, new Observer<List<AnswerTable>>() {
                @Override
                public void onChanged(List<AnswerTable> signTables) {
                    if (signTables.size() > 0) {
                        textView(signTables, layoutParams);
                    }
                }
            });
            ; //счетчик пункта решенных задач

//        while (myCursor.moveToNext()) { //цикл строк в таблице
//            llDBRES.addView(textView(myCursor, numb++, switchItem(myCursor), layoutParams), layoutParams); //добавления объекта textview в layout
//        }
//        myCursor.close();
    }

    public int switchItem(AnswerTable answerTable) {
        int iAn = 0;
        switch (answerTable.getAppraisal()) {
            case "отлично":
                iAn = 5;
                break;
            case "хорошо":
                iAn = 4;
                break;
            case "удволетворительно":
                iAn = 3;
                break;
            case "неудволетворительно":
                iAn = 2;
                break;
        }
        return iAn;
    }

    @SuppressLint("SetTextI18n")//хз, что за аннатация)))
    public void textView(List<AnswerTable> answerTables, LinearLayout.LayoutParams layoutParams) {
        for (AnswerTable answerTable: answerTables){
            TextView textView = new TextView(this); //создание объекта текста при каждом цикле новый
            textView.setText(answerTable.getId() + ") " + "Параграф " + answerTable.getNameParagraph() + "\n"
                    + "Оценка " + switchItem(answerTable) +
                    " (" + answerTable.getAppraisal() + ")\n"); //ПРисвоенние текста
            textView.setTextSize(20); //размер
            textView.setGravity(View.TEXT_ALIGNMENT_CENTER);// определения положение текста
            textView.setLayoutParams(layoutParams); //присвоенние лаяута
            llDBRES.addView(textView, layoutParams);
        }
    }
}