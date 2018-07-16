package com.example.user.project;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Login extends AppCompatActivity {

    EditText idText, tallText, weightText, ageText, goalText;
    Intent intent;

    //몸무게 db사용
    WeightDB weightHelper;
    SQLiteDatabase weightdb;

    //오늘 날짜 받기
    long now;
    Date date;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    String strdate, sql;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //액션바 숨기기
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        idText = (EditText) findViewById(R.id.idText);
        tallText = (EditText) findViewById(R.id.tallText);
        weightText = (EditText) findViewById(R.id.weightText);
        ageText = (EditText) findViewById(R.id.ageText);
        goalText = (EditText) findViewById(R.id.goalText);

        //db 입력하기 위한 선언
        weightHelper = new WeightDB(this);
        weightdb = weightHelper.getWritableDatabase();

        //현재 시간 받기
        now = System.currentTimeMillis();
        date = new Date(now);
        strdate = sdf.format(date);
    }

    String id="",tall="", weight="", gender="", age ="", goal = "";

    //성별 클릭 이벤트
    public void onGenderClick(View v) {
        switch (v.getId()) {
            case R.id.genderWoman:
                gender = ((RadioButton) v).getText().toString();
                break;
            case R.id.genderMan:
                gender = ((RadioButton) v).getText().toString();
                break;
        }
    }

    //가입 버튼 누르면 사용자가 입력한 정보를 가지고 main화면으로 감
    public void onClick(View v) {
        int no = 1;
        id = idText.getText().toString();
        tall = tallText.getText().toString();
        weight = weightText.getText().toString();
        age = ageText.getText().toString();
        goal = goalText.getText().toString();

        //몸무게 db에 입력
        sql = "insert into weight(weight, date) values(";
        sql += "'"+weight+"',";
        sql += "'"+strdate+"')";
        weightdb.execSQL(sql);

        intent = new Intent(Login.this,MainActivity.class);
        intent.putExtra("no",no);
        intent.putExtra("id",id);
        intent.putExtra("tall",tall);
        intent.putExtra("weight",weight);
        intent.putExtra("gender",gender);
        intent.putExtra("age",age);
        intent.putExtra("goal",goal);

        startActivity(intent);
    }
}
