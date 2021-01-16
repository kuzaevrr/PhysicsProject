package com.alina.physicsproject.activity;

import android.content.Intent;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.alina.physicsproject.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Paragraph extends AppCompatActivity {
    private int indexTheme; //id выбранного параграфа
    private String idTheme; //переменная id параграфа
    private ArrayList<String> parserListPlan, parserListText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paragraph);
        ListView listButton;
        {
            listButton = findViewById(R.id.listParagraph);
            parserListPlan = new ArrayList<>();
            parserListText = new ArrayList<>();
        }

        indexTheme = Integer.parseInt(getIntent().getStringExtra("index"));

        List<List<String>> oneList = (List<List<String>>) getIntent().getSerializableExtra("One");
        List<List<String>> twoList = (List<List<String>>) getIntent().getSerializableExtra("Two");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                twoList.get(indexTheme) // Список тем в параграфе
        );


        listButton.setAdapter(adapter);
        listButton.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) { //обработчик нажатия каждой темы

                idTheme =  oneList.get(indexTheme).get(position).split("\\(")[1].split("\\)")[0]; //id оглавления

                try {
                    InputStream myInputPlan = getApplicationContext().getAssets().open(idTheme + "/" + "Plan.txt");
                    InputStream myInputText = getApplicationContext().getAssets().open(idTheme + "/" + "Text.txt");
                    myInputPlan.close();
                    myInputText.close();

                    parserFileAndGeneration("Plan.txt");
                    parserFileAndGeneration("Text.txt");

                    intentOutSpisokPar(twoList, position, getIntent().getStringExtra("userName"));
                } catch (IOException e) {
                    Toast.makeText(getApplicationContext(), getString(R.string.error_501), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void intentOutSpisokPar(List<List<String>> twoList, int position, String user){
        Intent intent = new Intent(this, Activity_Spisok_Par.class);
        intent.putExtra("userName", user);
        intent.putExtra("nameTheme", twoList.get(indexTheme).get(position));//передача наименнования темы
        intent.putExtra("idTheme", idTheme);
        intent.putExtra("parserListPlan", parserListPlan);
        intent.putExtra("parserListText", parserListText);
        startActivity(intent);// запуск activity
    }

    public void parserFileAndGeneration(String nameFile) { //парсер из файла
        ArrayList<String> result = new ArrayList<>();
        try {
            InputStream myInput = Paragraph.this.getAssets().open(idTheme + "/" + nameFile); //путь к файлу
            BufferedReader br = new BufferedReader(new InputStreamReader(myInput));
            String line;
            StringBuilder strB = null;
            int i = 1;
            int idI = 1;
            while ((line = br.readLine()) != null) { //считываем каждую строчку
                if (nameFile.equals("Plan.txt")) {//проверка пункта
                    if (i > 1) result.add(strB.toString());
                    i++;
                    strB = new StringBuilder();
                } else if (nameFile.equals("Text.txt")) {
                    strB = new StringBuilder();
                }
                if (line.contains("#" + idTheme + idI + "#") && nameFile.equals("Plan.txt")) {
                    result.add(strB.toString());
                    strB = new StringBuilder();
                    strB.append(line);
                    result.add(strB.toString());
                    strB = new StringBuilder();
                    idI++;
                } else if (line.contains("#" + idTheme + idI + "#") && nameFile.equals("Text.txt")) {
                    strB.append(line);
                    result.add(strB.toString());
                } else if (nameFile.equals("Plan.txt")) {
                    strB.append(line);
                } else if (nameFile.equals("Text.txt")) {
                    strB.append(line);
                    result.add(strB.toString());
                }
            }
            if (nameFile.equals("Plan.txt")) result.add(strB.toString());
            for (String s : result) Log.e("abz", s);
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (nameFile.equals("Plan.txt")) parserListPlan = result; //из файла Plan
        if (nameFile.equals("Text.txt")) parserListText = result; //из файла Text
    }
}



