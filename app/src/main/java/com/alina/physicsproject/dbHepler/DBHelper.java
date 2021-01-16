package com.alina.physicsproject.dbHepler;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    public static final int Database_Version = 1; //версии БД
    public static final String Database_Name = "SQLliteDBName"; //Имя БД
    public static final String Table_Answer = "tableAnswer"; //таблица Ответов
    public static final String Table_Sign = "tableSign"; //таблица авторизации


    public static final String KEY_NAME = "name";
    public static final String KEY_MAIL = "mail";

    public static final String NAME_PARAGRAPH = "nameParagraph";
    public static final String STRING_APRAISAL = "strApraisal";


    public DBHelper(Context context) {
        super(context, Database_Name, null, Database_Version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) { //создание таблиц
        db.execSQL("CREATE TABLE " + Table_Answer + "(" +
                NAME_PARAGRAPH + " text, " +
                STRING_APRAISAL + " text " +
                " );");

        db.execSQL("CREATE TABLE " + Table_Sign + "(" +
                KEY_NAME + " text, " +
                KEY_MAIL + " text" +
                " );");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { //создание новой версии
        db.execSQL("drop table if exists " + Table_Answer);
        db.execSQL("drop table if exists " + Table_Sign);
        onCreate(db);
    }
}
