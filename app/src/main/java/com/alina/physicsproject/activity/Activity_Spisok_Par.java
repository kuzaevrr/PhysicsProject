package com.alina.physicsproject.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import com.alina.physicsproject.R;
import com.alina.physicsproject.justifiedTextView.JustifiedTextView;

public class Activity_Spisok_Par extends AppCompatActivity {


    private LinearLayout ll;
    private TextView tx;
    private String message;
    private String loginName, idTheme;
    private ArrayList<String> parserListPlan, parserListText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paragraph_text_info);

        { //блок инициализации (для повышкения читаемости) ;)
            tx = findViewById(R.id.textItem);
            ll = findViewById(R.id.llMAin);
            loginName = getIntent().getStringExtra("userName");
            idTheme = getIntent().getStringExtra("idTheme");
            message = getIntent().getStringExtra("nameTheme");
            parserListPlan = getIntent().getStringArrayListExtra("parserListPlan");
            parserListText = getIntent().getStringArrayListExtra("parserListText");
        }
        parserText(parserListPlan);
    }


    @SuppressLint("NonConstantResourceId")//хз, что за анатация))
    public void сhanges(View view) { //обработчик кнопок
        // Fragment fragment = null;
        switch (view.getId()) {
            case R.id.planFr: //кнопка план
                ll.removeAllViews();
                parserText(parserListPlan);
                break;
            case R.id.textFr: //кнопка текст
                ll.removeAllViews();
                parserText(parserListText);
                break;
            case R.id.infroFr: //кнопка инфорграфика
                ll.removeAllViews();
                setResourceInfografika();
                break;
        }
    }

    public void setTheme() { //метод теме имя
        tx.setText(message);
        tx.setTextColor(Color.BLACK);
        tx.setWidth(0);
    }


    public void setTextJustfied(String text, int listSize) {//метод присвоение текста
        if (!text.equals(" ") && !text.equals("")) {
            Log.d("Text", text + " | " + listSize);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            layoutParams.setMargins(0, 5, 0, 0);

            JustifiedTextView justifiedTextView = new JustifiedTextView(this); //ообъект для расширения текста по всей ширине
            justifiedTextView.setText(text);
            justifiedTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
            justifiedTextView.setPadding(15, 15, 15, 15);
            justifiedTextView.setAlignment(Paint.Align.LEFT);
            ll.addView(justifiedTextView, layoutParams);
        }
    }

    public void setImage(Drawable d) { //присвоение картинки из файла
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.setMargins(0, 15, 0, 0);
        ImageView ivNew = new ImageView(this);
        ivNew.setAdjustViewBounds(true);
        ivNew.setImageDrawable(d);
//        ivNew.setScaleType(ImageView.ScaleType.CENTER_CROP);
        ll.addView(ivNew, layoutParams);
    }

    public Button newButton() {
        Button btTest = new Button(this); //создание кнопки тест
        btTest.setText("Тест");
        btTest.setTextSize(18);
        btTest.setBackgroundResource(R.drawable.drawable_style_button); //стиль кнопки
        btTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //обработчик нажатия
                if (!loginName.equals(getString(R.string.no_user_login)) && loginName != null) { //условие авторизации
                    Intent intObj = new Intent(v.getContext(), TestActivity.class);//Lk
                    intObj.putExtra("userName", loginName);
                    intObj.putExtra("message", message);
                    intObj.putExtra("idTheme", idTheme);
                    startActivity(intObj);
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Необходимо авторизоваться!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return btTest;
    }

    public void parserText(ArrayList<String> list) { //парсер текста
        setTheme(); //метод теме имя
        int listSize = 0;
        LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        for (String text : list) { //проверка каждой строки
            if (!text.contains("#")) {
                setTextJustfied(text, ++listSize); //метод генерации текста
            } else if (text.contains("#")) {
                String[] rsh = {".jpg", ".JPG", ".PNG", ".png"}; //разный формат картинок
                for (int i = 0; i < 4; i++) {
                    try {
                        String idImage = text.split("#")[1].split("#")[0];
                        InputStream myInput = Activity_Spisok_Par.this.getAssets().open(idTheme + "/" + idImage + rsh[i]);//поиск файла
                        Drawable drawable = Drawable.createFromStream(myInput, "null");
                        setImage(drawable); //генерация картинки из файла
                        myInput.close();
                        break;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        ll.addView(newButton(), lParams);
    }

    public void setResourceInfografika() {
        try {
            ImageView ivNew = new ImageView(this);
            InputStream myInput = Activity_Spisok_Par.this.getAssets().open(idTheme + "/" + "Infografika.JPG");//поиск файла
            Drawable d = Drawable.createFromStream(myInput, "null");
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT); // размер картинки
            ivNew.setLayoutParams(layoutParams);
            ivNew.setAdjustViewBounds(true);
            ivNew.setScaleType(ImageView.ScaleType.CENTER_CROP);
            ivNew.setImageDrawable(d);
            ll.addView(ivNew, layoutParams);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}