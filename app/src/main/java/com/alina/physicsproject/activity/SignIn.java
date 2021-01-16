package com.alina.physicsproject.activity;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.alina.physicsproject.R;
import com.alina.physicsproject.sendMail.SendMail;
import com.alina.physicsproject.dbHepler.DBHelper;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.alina.physicsproject.dbHepler.DBHelper.KEY_MAIL;
import static com.alina.physicsproject.dbHepler.DBHelper.KEY_NAME;
import static com.alina.physicsproject.dbHepler.DBHelper.Table_Sign;


public class SignIn extends AppCompatActivity {
    private EditText nameUser, mailUser;
    private Button upButton, signOutput;
    private boolean boolClickOpenKey;
    private String loginNameIsMain;
    private String genPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        {
            nameUser = (EditText) findViewById(R.id.userNameMail); //имя юзера (editText)
            mailUser = (EditText) findViewById(R.id.userMail); //маил юзера (editText)
            upButton = (Button) findViewById(R.id.signup);
            signOutput = (Button) findViewById(R.id.signOutput);
        }

        //проверка активации
        checkAuthorization();
    }

    public void checkAuthorization() {
        DBHelper dbHelper = new DBHelper(this);
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        String nameUserS = null;
        String mailUserS = null;
        Cursor myCursor = database.query(Table_Sign, null, null, null, null, null, null);
        while (myCursor.moveToNext()) {
            Log.d("SQLSIGNInput", myCursor.getString(0) + "|" + myCursor.getString(1));
            nameUserS = myCursor.getString(0);
            mailUserS = myCursor.getString(1);
        }
        if (nameUserS != null) {
            upButton.setEnabled(false);
            signOutput.setEnabled(true);
            nameUser.setHint(nameUserS);
            mailUser.setHint(mailUserS);
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Вы авторизованы!", Toast.LENGTH_SHORT);
            toast.show();
            //отключение кнпоки при авторизации
        } else {
            signOutput.setEnabled(false);
            upButton.setEnabled(true);
        }
        myCursor.close();
        database.close();
        dbHelper.close();

    }

    public void signUp(View v) {
        Pattern pattern = Pattern.compile(".+@.+\\."); //Патерн регулярного выражения
        Matcher matcher = pattern.matcher(mailUser.getText().toString()); //матчер
        dispatchToMailPassword(matcher);
    }

    public void dialog() {//создание диалогового окна

        AlertDialog.Builder builder = new AlertDialog.Builder(SignIn.this);
        builder.setTitle("Подтерждение почты!");
        builder.setMessage("Введите пароль подтверждения с почты");
        builder.setCancelable(false);

        final EditText passwordCode = new EditText(this);
        builder.setView(passwordCode);

        dialogButtonOpen(passwordCode, builder);
        dialogButtonClose(builder);

        AlertDialog alert = builder.create();
        alert.show();
    }


    public void dialogButtonClose(AlertDialog.Builder builder) {
        builder.setNegativeButton("Закрыть",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        boolClickOpenKey = false;
                    }
                });
    }


    public void dialogButtonOpen(EditText passwordCode, AlertDialog.Builder builder) {
        builder.setPositiveButton("Подтвердить", new DialogInterface.OnClickListener() { // Кнопка ОК
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (truePassword(passwordCode.getText().toString().trim())) {
                    boolClickOpenKey = true;

                    Pattern pattern = Pattern.compile(".+@.+\\."); //Патерн регулярного выражения
                    Matcher matcher = pattern.matcher(mailUser.getText().toString().trim()); //матчер
                    logicSignUp(matcher);
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Вы ввели, не правильно подтверждения, введите повторно", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    public void signOutput(View v) {//переавторизация
        upButton.setEnabled(true);
        signOutput.setEnabled(false);
        loginNameIsMain = getString(R.string.no_user_login);
        deletedUserIsDB();
    }

    public void deletedUserIsDB() {
        DBHelper dbHelper = new DBHelper(this);
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        database.delete(Table_Sign, null, null);
        dbHelper.close();
        database.close();
    }


    public String generation() {//Генератор пароля
        return Integer.toString(1000 + new Random().nextInt(8999));
    }

    public boolean truePassword(String inputPass) {
        if (inputPass.equals(genPassword)) {
            return true;
        } else {
            return false;
        }
    }

    public void logicSignUp(Matcher matcher) { //
        if (nameUser.getText().toString().split(" ").length == 2
                && matcher.find() && boolClickOpenKey) {

            upButton.setEnabled(false);
            signOutput.setEnabled(true);
            Toast.makeText(getApplicationContext(), "Успешно авторизованы", Toast.LENGTH_SHORT).show();
            insertDB();
            selectDBToNameUser();
            super.finish();
        }
    }

    public void dispatchToMailPassword(Matcher matcher) {
        if (matcher.find() && nameUser.getText().toString().split(" ").length == 2) {
            new SendMail(SignIn.this, mailUser.getText().toString().trim(),
                    "Пароль подтверждения.", "Ваш пароль подтверждения: " + (genPassword = generation())).execute();
            dialog();
        }
    }


    public void insertDB() {
        DBHelper dbHelper = new DBHelper(this);
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_NAME, nameUser.getText().toString().trim());
        contentValues.put(KEY_MAIL, mailUser.getText().toString().trim());
        database.insert(Table_Sign, null, contentValues);

        dbHelper.close();
    }

    public void selectDBToNameUser(){
        DBHelper dbHelper = new DBHelper(this);
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        Cursor myCursor = database.query(Table_Sign, null, null, null, null, null, null);
        while (myCursor.moveToNext()) {
            loginNameIsMain = myCursor.getString(0);
        }
        myCursor.close();
        dbHelper.close();
        database.close();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("userName", loginNameIsMain);
    }
}