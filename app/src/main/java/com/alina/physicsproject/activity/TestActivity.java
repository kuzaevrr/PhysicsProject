package com.alina.physicsproject.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.alina.physicsproject.R;
import com.alina.physicsproject.data.object.AnswerTable;
import com.alina.physicsproject.data.viewModels.AnswerViewModel;
import com.alina.physicsproject.sendMail.SendMail;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class TestActivity extends AppCompatActivity {

    private Button btNext, btBack, Answer, buttonResult;
    private EditText editTextV;
    private ImageView imageTest;
    private TextView tx, txV, resTestView;
    private ListView lvO;
    private LinearLayout linearLayout;
    private SeekBar seekBar;
    private String appraisal;
    private int appraisalNumb;
    //переменные парсера
    private List<List<String>> listQuestion;
    private String[] listQuestionAnswer;//Правильный ответ выборанный в активити (позиция листа)
    private ArrayList<String> textTypeQuestion; //вопросы
    private ArrayList<String> typeQuestion; //тип вопроса слогласно последовательности
    private ArrayList<String> correctQuestion; //Правильный ответ парера
    private HashMap<Integer, String> imageQuestion; //картинка в вопросе
    private HashMap<String, String> hashMap;

    private AnswerViewModel answerViewModel;
    private List<Integer> score; //позиция выбора цвета
    private int scoreSP = 0;
    private String loginName;
    private String idTheme;
    private String message;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {//Меттод активити
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        {
            score = new ArrayList<>();
            hashMap = new HashMap<>();
            listQuestion = new ArrayList<>();
            imageQuestion = new HashMap<>();

            btNext = findViewById(R.id.buttonNext);
            btBack = findViewById(R.id.buttonBack);
            tx = findViewById(R.id.oglTest);
            linearLayout = findViewById(R.id.layoutTest);
            lvO = findViewById(R.id.listO);
            txV = findViewById(R.id.vopros);
            buttonResult = findViewById(R.id.buttonResult);
            seekBar = findViewById(R.id.seekBar);
            resTestView = findViewById(R.id.textViewResult);
            imageTest = findViewById(R.id.imageVopros);
            hashMap.put("0", "а");
            hashMap.put("1", "б");
            hashMap.put("2", "в");
            hashMap.put("3", "г");
            hashMap.put("4", "д");
            hashMap.put("5", "е");
            buttonResult.setClickable(false);
            buttonResult.setVisibility(View.INVISIBLE);
            answerViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(AnswerViewModel.class);
        }


        Intent intent = getIntent();//создание интента
        loginName = intent.getStringExtra("userName");
        idTheme = intent.getStringExtra("idTheme");
        message = intent.getStringExtra("message");

        tx.setText(getString(R.string.activity_test_name_to_test) + message);

        try {

            parserTest(); //парсер вопросов
            listQuestionAnswer = new String[textTypeQuestion.size()];
            logicSp(listQuestion.get(scoreSP), textTypeQuestion.get(scoreSP));

        } catch (IOException e) {
            e.getMessage();
        }

        newSeekbar();
    }

    public void newSeekbar() {
        seekBar.setMax(textTypeQuestion.size() - 1);
        seekBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        tx.setTextColor(Color.BLACK);
        txV.setTextColor(Color.BLACK);
        resTestView.setTextColor(Color.BLACK);
    }

    public void check(int position, View view, List<Integer> ix) { //Отображения цветового ответа списка
        if (score.get(position) == 0) {
            view.setBackgroundColor(Color.rgb(195, 213, 255));
            ix.add(view.getId());
            score.add(position, 1);
        } else if (score.get(position) == 1) {
            view.setBackgroundColor(Color.TRANSPARENT);
            ix.add(view.getId());
            score.add(position, 0);
        }
    }

    public void check(String position) { //Отображение ответа на предыдущий овтет
        String res = null;
        if (position != null) {
            switch (position) {
                case "0":
                    res = "а)";
                    break;
                case "1":
                    res = "б)";
                    break;
                case "2":
                    res = "в)";
                    break;
                case "3":
                    res = "г)";
                    break;
                case "4":
                    res = "д)";
                    break;
                case "5":
                    res = "e)";
                    break;
            }
            if (typeQuestion.get(scoreSP).equals("1")) {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "В данном вопросе выбран ответ: " + res, Toast.LENGTH_SHORT);
                toast.show();
            } else if (typeQuestion.get(scoreSP).equals("2")) {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "В данном вопросе выбран ответ: " + position, Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }

    public void clickNext(View v) {// обработчик кнопкаи следующий вопрос
        scoreSP++;
        try {
            check(listQuestionAnswer[scoreSP]);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        logicSp(listQuestion.get(scoreSP), textTypeQuestion.get(scoreSP));
    }

    public void clickBack(View v) {//Обработчик предыдущий вопрос
        scoreSP--;
        check(listQuestionAnswer[scoreSP]);
        logicSp(listQuestion.get(scoreSP), textTypeQuestion.get(scoreSP));
        resTestView.setText("");
    }


    public void deletedView() {
        linearLayout.removeView(Answer);
        linearLayout.removeView(editTextV);
    }

    public void newImage(Drawable drawable) {
        imageTest.setAdjustViewBounds(true);
        imageTest.setImageDrawable(drawable);
//        imageTest.setScaleType(ImageView.ScaleType.CENTER_CROP);
    }

    public void logicSp(final List<String> list, String stringV) {//Логика отоброжения вопроса!!!


        deletedView();
        seekBar.setProgress(scoreSP);
        String[] rsh = {".jpg", ".JPG", ".PNG", ".png"};

        for (int i = 0; i < 4; i++) {
            try {
                InputStream myInput = TestActivity.this.getAssets().open(idTheme + "/" + imageQuestion.get(scoreSP + 1) + rsh[i]);//поиск файла
                Drawable drawable = Drawable.createFromStream(myInput, "null");
//говно код)
                newImage(drawable);//новая картинка
                //Добавление картинки в активити
                Log.d("Номер картинки в логике", imageQuestion.get(scoreSP + 1));
                break;
            } catch (IOException e) {
                e.printStackTrace();
                imageTest.setImageDrawable(null);
            }
        }

        if (scoreSP == 0) {
            btBack.setClickable(false);
        } else if (scoreSP > 0) {
            btBack.setClickable(true);
        }
        if (scoreSP + 1 == textTypeQuestion.size()) {
            buttonResult.setVisibility(View.VISIBLE);
            btNext.setClickable(false);
        } else if (scoreSP + 1 < textTypeQuestion.size()) {
            btNext.setClickable(true);
            buttonResult.setClickable(false);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>( //Генерируется список вариантов ответа
                this,
                android.R.layout.simple_list_item_1,
                list
        );


        testTypeTwo(); //Создание вопроса второго типа
        txV.setText(stringV);
        txV.setTextSize(18); //размер текста вопроса

        for (int i = 0; i < listQuestion.get(scoreSP).size(); i++) { //заполненеие листа
            score.add(0);
        }


        lvO.setAdapter(adapter);
        lvO.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // отображение выбранного ранее ответа!
                if (scoreSP + 1 == textTypeQuestion.size()) { //активация кнопки завершить тест!
                    buttonResult.setClickable(true);
                }
                List<Integer> ix = new ArrayList<>();

                listQuestionAnswer[scoreSP] = Integer.toString(position);
                check(position, view, ix);

            }
        });
    }

    public void testTypeTwo() {
        if (typeQuestion.get(scoreSP).equals("2")) {//Условие второго типа вопроса
            editTextV = new EditText(this);
            editTextV.setHint("Введите значение! После чего нажмите на кнопку \"Ответить на вопрос\" ");

            Answer = new Button(this);
            Answer.setText("Ответить на вопрос!");
            Answer.setBackgroundResource(R.drawable.drawable_style_button);
            Answer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listQuestionAnswer[scoreSP] = String.valueOf(editTextV.getText()).trim();
                }
            });

            LinearLayout.LayoutParams layoutTest = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            linearLayout.addView(editTextV, layoutTest);
            linearLayout.addView(Answer, layoutTest);
        }
    }

    public void parserTest() throws IOException { //парсер Теста
        typeQuestion = new ArrayList<>();  // тип вопроса согласно последовательности
        textTypeQuestion = new ArrayList<>(); // Текст вопроса
        correctQuestion = new ArrayList<>(); // Правильный ответ у каждого вопроса свой по последовательности


        InputStream myInput = TestActivity.this.getAssets().open(idTheme + "/" + "Test.txt"); //путь к файлу
        BufferedReader br = new BufferedReader(new InputStreamReader(myInput));

        String lineReaderTest;
        int quer = 0;
        int numberV = 0;
        ArrayList<String> oneQuentionList = new ArrayList<>();

        while ((lineReaderTest = br.readLine()) != null) {
            if (lineReaderTest.contains("#" + idTheme)) {

                StringBuilder sb = new StringBuilder(lineReaderTest);
                sb.deleteCharAt(0);
                sb.deleteCharAt(lineReaderTest.length() - 2);
                imageQuestion.put(numberV, sb.toString());
                continue;
            }
            if (lineReaderTest.contains("Option")) {
                numberV++;
                quer = 0;
                String[] sSplit = lineReaderTest.split("_");//разбитие строки на номер вопроса и тип

                typeQuestion.add(sSplit[1]); //тип вопроса
            }

            if (quer == 1 && !lineReaderTest.contains("#")) {
                textTypeQuestion.add(lineReaderTest);
            } else if (quer > 1 && !lineReaderTest.contains("Answer")) {
                oneQuentionList.add(lineReaderTest);

            } else if (lineReaderTest.contains("Answer")) {

                List<String> list = new ArrayList<>();
                for (String s : oneQuentionList) {
                    list.add(s);
                } //cписок ответов по вопросам
                listQuestion.add(list);
                oneQuentionList.clear();

                correctQuestion.add(lineReaderTest.split("Answer: ")[1].trim());
            }
            if (!lineReaderTest.contains("#")) quer++;
        }

        myInput.close();//Закрытие потока
        br.close();// Закрытие буфера
    }

    @SuppressLint("SetTextI18n")
    public void onClickRes(View v) {//Присвоение и передача оценки

        appraisal = null;
        appraisalNumb = 0;
        resTestView.setTextSize(20);
        double pr = 0;
        for (int resC = 0; resC < textTypeQuestion.size(); resC++) {
            if (correctQuestion.get(resC).equals(hashMap.get(listQuestionAnswer[resC])) || correctQuestion.get(resC).equals(listQuestionAnswer[resC])) {
                pr++;
            }
        }
        double z = pr / textTypeQuestion.size() * 100;
        if (z > 90) {
            appraisal = "отлично";
            appraisalNumb = 5;
            resTestView.setText(getString(R.string.text_to_appraisal) + getString(R.string.appraisal_five));
        } else if (z > 75 && z < 89) {
            appraisal = "хорошо";
            appraisalNumb = 4;
            resTestView.setText(getString(R.string.text_to_appraisal) + getString(R.string.appraisal_fo));
        } else if (z > 50 && z < 74) {
            appraisal = "удволетворительно";
            appraisalNumb = 3;
            resTestView.setText(getString(R.string.text_to_appraisal) + getString(R.string.appraisal_three));
        } else if (z < 50) {
            appraisal = "неудволетворительно";
            appraisalNumb = 2;
            resTestView.setText(getString(R.string.text_to_appraisal) + getString(R.string.appraisal_two));
        }

        answerViewModel.insertAnswer(new AnswerTable(message, appraisal));
        sendEmail();
        btBack.setClickable(false);
        buttonResult.setClickable(false);
    }


    private void sendEmail() {
        //Getting content for email
        String email = getString(R.string.result_test_studs_to_mail);
        String subject = "Тема: " + message + "\nУченик: " + loginName;
        String messages = "Тема: " + message + "\nУченик: " + loginName + "\n" + "Оценка " + appraisalNumb +
                " (" + appraisal + ")\n";
        //Creating SendMail object
        boolean keyFinish = false;
        try {
            keyFinish = new SendMail(this, email, subject, messages).execute().get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        //Executing sendmail to send email
//        if(keyFinish){
//            Toast.makeText(this, "Оценка за тест: "+ appraisalNumb, Toast.LENGTH_SHORT).show();
//            finish();
//        }
    }
}