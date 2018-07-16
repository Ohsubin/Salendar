package com.example.user.project;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Info extends AppCompatActivity{
    Intent intent;
    TextView idText, genderText, tallText, weightText, estimateText, levelText, ageText, goalText;
    String id, gender, strage, strtall, strweight, strestimate, strlevel, strgoal;
    double tall, weight, estimate, level, goal;
    int age;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        //액션바 숨기기
        ActionBar ab = getSupportActionBar();
        ab.hide();

        //layout 정의하기
        idText = (TextView) findViewById(R.id.idText);
        genderText = (TextView) findViewById(R.id.genderText);
        tallText = (TextView) findViewById(R.id.tallText);
        weightText = (TextView) findViewById(R.id.weightText);
        estimateText = (TextView) findViewById(R.id.estimateText);
        levelText = (TextView) findViewById(R.id.levelText);
        ageText = (TextView) findViewById(R.id.ageText);
        goalText = (TextView) findViewById(R.id.goalText);

        //메인에서 로그인 정보 얻어오기
        intent = getIntent();
        id = intent.getStringExtra("id");
        strage = intent.getStringExtra("age");
        gender = intent.getStringExtra("gender");
        strtall = intent.getStringExtra("tall");
        strweight = intent.getStringExtra("weight");
        strgoal = intent.getStringExtra("goal");

        //기초대사량, 비만도 계산하기
        Calc();

        //사용자 정보 띄우기
        idText.setText(id);
        ageText.setText(strage);
        genderText.setText(gender);
        tallText.setText(strtall);
        weightText.setText(strweight);
        estimateText.setText(strestimate);
        levelText.setText(strlevel);
        goalText.setText(strgoal);
    }

    //수정 버튼 누르면 수정화면으로 사용자 정보를 가지고 감
    public void onClick(View v) {
        intent = new Intent(Info.this,UpdateInfo.class);
        intent.putExtra("id",id);
        intent.putExtra("tall",strtall);
        intent.putExtra("weight",strweight);
        intent.putExtra("gender",gender);
        intent.putExtra("age",strage);
        intent.putExtra("goal",strgoal);
        startActivityForResult(intent,100);
    }

    //취소 버튼을 누르면 메인화면으로 사용자 정보를 가지고 감
    public void onresetClick(View v) {
        intent.putExtra("id",id);
        intent.putExtra("gender",gender);
        intent.putExtra("tall",strtall);
        intent.putExtra("weight",strweight);
        intent.putExtra("age",strage);
        intent.putExtra("goal",strgoal);
        setResult(8,intent);
        finish();
    }

    //수정된 사용자 정보를 받아서 띄움
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 100) {
            if(resultCode == 7) {
                id = data.getStringExtra("id");
                strage = data.getStringExtra("age");
                gender = data.getStringExtra("gender");
                strtall = data.getStringExtra("tall");
                strweight = data.getStringExtra("weight");
                strgoal = data.getStringExtra("goal");

                Calc();

                idText.setText(id);
                ageText.setText(strage);
                genderText.setText(gender);
                tallText.setText(strtall);
                weightText.setText(strweight);
                estimateText.setText(strestimate);
                levelText.setText(strlevel);
                goalText.setText(strgoal);
            }
        }
    }

    //기초 대사량, 비만도 계산
    public void Calc() {
        weight = Double.parseDouble(strweight);
        tall = Double.parseDouble(strtall);
        age = Integer.parseInt(strage);
        goal = Integer.parseInt(strgoal);

        if(gender.equals("남성"))
            estimate = (66.47 + (13.75*weight)+(5*tall) - (6.76*age));
        else
            estimate = (655.1 + (9.56*weight)+(1.855*tall)-(4.68*age));

        level = (weight/(tall*tall))*10000;

        //비만도에 따라 등급 나눔,  %.2f는 소수점 2번째 자리까지 나오게
        if(level <= 18.4) {
            strlevel = String.format("%.2f",level) + " (저체중)";
        } else if(level >= 18.5 && level <= 23.9) {
            strlevel = String.format("%.2f",level) + " (정상)";
        } else if(level >= 23.0 && level <=24.9) {
            strlevel = String.format("%.2f",level) + " (과체중)";
        }else if(level >= 25.0 && level <= 29.9) {
            strlevel = String.format("%.2f",level) + " (경도비만)";
        }else  if(level >= 30) {
            strlevel = String.format("%.2f",level) + " (중등도비만)";
        }else
            strlevel = "(에러)";

        strestimate = String.format("%.2f",estimate);

    }
}
