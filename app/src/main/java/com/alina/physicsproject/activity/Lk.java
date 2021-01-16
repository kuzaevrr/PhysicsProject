package com.alina.physicsproject.activity;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alina.physicsproject.R;
import com.alina.physicsproject.dbHepler.DBHelper;

public class Lk extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lk);

        LinearLayout llDBRES = (LinearLayout) findViewById(R.id.llDBRES);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        DBHelper dbHelper = new DBHelper(this); // объект DBHELPER обращение к БД
        SQLiteDatabase database = dbHelper.getWritableDatabase(); //Создание объекта БД
        Cursor myCursor = database.query(DBHelper.Table_Answer, //таблица в БД
                null, null, null, null, null, null);

        int numb = 1; //счетчик пункта решенных задач
        while (myCursor.moveToNext()) { //цикл строк в таблице
            llDBRES.addView(textView(myCursor, numb++, switchItem(myCursor), layoutParams), layoutParams); //добавления объекта textview в layout
        }
        myCursor.close();
    }

    public int switchItem(Cursor myCursor) {
        int iAn = 0;
        switch (myCursor.getString(1)) {
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
    public TextView textView(Cursor myCursor, int numb, int switchItem, LinearLayout.LayoutParams layoutParams) {
        TextView textView = new TextView(this); //создание объекта текста при каждом цикле новый
        textView.setText(numb + ") " + "Параграф " + myCursor.getString(0) + "\n"
                + "Оценка " + switchItem +
                " (" + myCursor.getString(1) + ")\n"); //ПРисвоенние текста
        textView.setTextSize(20); //размер
        textView.setGravity(View.TEXT_ALIGNMENT_CENTER);// определения положение текста
        textView.setLayoutParams(layoutParams); //присвоенние лаяута
        return textView;
    }
}