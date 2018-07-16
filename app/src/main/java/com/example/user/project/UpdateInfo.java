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

public class UpdateInfo extends AppCompatActivity {
    Intent intent;
    String id, gender, strage, strtall, strweight, strgoal, strdate, sql;
    EditText idText, tallText, weightText, ageText, goalText;
    RadioButton radiowoman, radioman;

    WeightDB weightHelper;
    SQLiteDatabase weightdb;

    long now;
    Date date;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_info);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        idText = (EditText) findViewById(R.id.idText);
        tallText = (EditText) findViewById(R.id.tallText);
        weightText = (EditText) findViewById(R.id.weightText);
        ageText = (EditText) findViewById(R.id.ageText);
        goalText = (EditText) findViewById(R.id.goalText);
        radioman = (RadioButton) findViewById(R.id.genderMan);
        radiowoman = (RadioButton) findViewById(R.id.genderWoman);

        intent = getIntent();
        id = intent.getStringExtra("id");
        strage = intent.getStringExtra("age");
        gender = intent.getStringExtra("gender");
        strtall = intent.getStringExtra("tall");
        strweight = intent.getStringExtra("weight");
        strgoal = intent.getStringExtra("goal");

        idText.setText(id);
        tallText.setText(strtall);
        ageText.setText(strage);
        weightText.setText(strweight);
        goalText.setText(strgoal);

        if(gender.equals("남성")) {
            radioman.setChecked(true);
            radiowoman.setChecked(false);
        }else {
            radioman.setChecked(false);
            radiowoman.setChecked(true);
        }

        weightHelper = new WeightDB(this);
        weightdb = weightHelper.getWritableDatabase();

        now = System.currentTimeMillis();
        date = new Date(now);
        strdate = sdf.format(date);
    }

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

    public void onClick(View v) {
        id = idText.getText().toString();
        strtall = tallText.getText().toString();
        strweight = weightText.getText().toString();
        strage = ageText.getText().toString();
        strgoal = goalText.getText().toString();

        sql = "insert into weight(weight, date) values(";
        sql += "'"+strweight+"',";
        sql += "'"+strdate+"')";
        weightdb.execSQL(sql);

        intent.putExtra("id",id);
        intent.putExtra("gender",gender);
        intent.putExtra("tall",strtall);
        intent.putExtra("weight",strweight);
        intent.putExtra("age",strage);
        intent.putExtra("goal",strgoal);
        setResult(7,intent);
        finish();
    }
}
