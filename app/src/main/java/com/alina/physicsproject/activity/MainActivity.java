package com.alina.physicsproject.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.alina.physicsproject.R;
import com.alina.physicsproject.data.object.SignTable;
import com.alina.physicsproject.data.viewModels.SignViewModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private TextView tvView, nameMailUser;
    private Button power, press, molecules, Archimedes, vved; //создание объектов view
    private String index;  //Индекс паракрафа
    private String loginName; //Имя ученика
    private final ArrayList<String> result = new ArrayList<>();
    private final List<List<String>> listListTwoOGLResTextOGL = new ArrayList<>();
    private final List<List<String>> listListOneOGLResTextOGL = new ArrayList<>();
    private SignViewModel signViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        {
            tvView = (TextView) findViewById(R.id.tvView); //присовение view
            power = (Button) findViewById(R.id.button18);
            press = (Button) findViewById(R.id.button16);
            molecules = (Button) findViewById(R.id.button15);
            Archimedes = (Button) findViewById(R.id.button17);
            vved = (Button) findViewById(R.id.button19);
            nameMailUser = (TextView) findViewById(R.id.userNameMail);
            signViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(SignViewModel.class);
        }
        typeInit();
        checkLoginPassword(); //метод проверки содержания логина и пароля
    }

    @SuppressLint("SetTextI18n")
    public void checkLoginPassword() {
        LiveData<List<SignTable>> listLiveData = signViewModel.getListLiveData();
        listLiveData.observe(this, new Observer<List<SignTable>>() {
            @Override
            public void onChanged(List<SignTable> signTables) {
                if (signTables.size() > 0) {
                    loginName = signTables.get(0).getUserName();
                    tvView.setText(getString(R.string.activity_main_study) + " " + loginName);
                }
            }
        });
    }

    public void signIn(View v) { //обработчик для кнопки "АВТОРИЗАЦИЯ"

        startActivity(new Intent(this, SignIn.class));
    }

    public void buttonVV(View v) {//КНопка для парагарафа
        index = "0";
        intentRequest(new Intent(this, Paragraph.class));
    }

    public void newScreenOne(View v) {//КНопка для парагарафа
        index = "1";
        intentRequest(new Intent(this, Paragraph.class));
    }

    public void newScreenTwo(View v) {//КНопка для парагарафа
        index = "2";
        intentRequest(new Intent(this, Paragraph.class));
    }

    public void newScreenThree(View v) {//КНопка для парагарафа
        index = "3";
        intentRequest(new Intent(this, Paragraph.class));
    }

    public void newScreenFo(View v) {//КНопка для парагарафа
        index = "4";
        intentRequest( new Intent(this, Paragraph.class));
    }

    public void newScreenSpravka(View v) { //КНопка для справки
        startActivity(new Intent(this, Spravka.class));
    }

    public void lk(View v) { //кнопка личный кабинет
        startActivity(new Intent(this, Lk.class));
    }

    @SuppressLint("SetTextI18n")
    @Override //переопределение метода
    public void onResume() {//метод выполняется перед отображением активити
        super.onResume();
        loginName = getIntent().getStringExtra("userName");
        if (loginName == null) {
            loginName = getString(R.string.no_user_login);
        }
        tvView.setText(getString(R.string.activity_main_study) + " " + loginName);
        checkLoginPassword();//присвоение новоого имени
    }

    public void intentRequest(Intent intent) {
        intent.putExtra("index",
                index);
        parserFileMain();
        intent.putExtra("userName", loginName);
        intent.putExtra("One", (Serializable) listListOneOGLResTextOGL);
        intent.putExtra("Two", (Serializable) listListTwoOGLResTextOGL);
        startActivity(intent);
    }

    public void parserFileMain() { //парсер файла namepar.txt
        try {
            Log.d("ParserMain", "YES");
            BufferedReader reader = new BufferedReader(new InputStreamReader(getResources().openRawResource(R.raw.namepar))); // запихивание файла в поток буффера данных чтобы читать по строчно
            String lineParsingFileToList;
            int oneLevelParsing = 0;
            List<String> listOne = new ArrayList<>();
            while ((lineParsingFileToList = reader.readLine()) != null) { // объем буфера
                result.add(lineParsingFileToList); // считываем остальные строки в цикле, добавление всего файла в один массив
                if (!result.get(oneLevelParsing).contains("--") //услвоие саодержания заголовка
                        && result.get(oneLevelParsing).split("\\(")[0].length() >= 1
                        && result.get(oneLevelParsing).split("\\)")[0].length() >= 1
                ) {

                    listOne.add(result.get(oneLevelParsing));
                    //добавление полного списка оглавлений по темна
                    if (result.get(oneLevelParsing).contains("Итоговый тест")) {
                        listListOneOGLResTextOGL.add(listOne);
                        listOne = new ArrayList<>();
                    }
                }
                oneLevelParsing++;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //////////////////////////////////////////////////////   Начало блока  для сощдания массива строгой длины //////////////////////////
        for (int i = 0; i < listListOneOGLResTextOGL.size(); i++) {
            for (String twoArrayToStringLine : listListOneOGLResTextOGL.get(i)) { // цикл разбитие на темы
                if (twoArrayToStringLine.contains("Итоговый")) {    //условие окончание темы
                    break;
                }
            }
            List<String> listTwo = new ArrayList<>();

            for (String stringOneOGLResTextOGL : listListOneOGLResTextOGL.get(i)) {
                listTwo.add(stringOneOGLResTextOGL.split("\\(")[0]);

                if (stringOneOGLResTextOGL.contains("Итоговый")) {
                    break;
                }
            }
            listListTwoOGLResTextOGL.add(listTwo);
        }
    }

    public void typeInit() {
        Typeface type = Typeface.createFromAsset(getAssets(), "font/title_text.ttf"); //открытие файла со шрифтом
        power.setTypeface(type); //присвоение шрифта view
        tvView.setTypeface(type);
        press.setTypeface(type);
        molecules.setTypeface(type);
        Archimedes.setTypeface(type);
        vved.setTypeface(type);
        nameMailUser.setTypeface(type);
    }
}
