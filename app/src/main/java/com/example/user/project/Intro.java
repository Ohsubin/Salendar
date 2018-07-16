package com.example.user.project;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Intro extends AppCompatActivity {
    public SharedPreferences prefs;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        //액션바 숨기기
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //다운 받고 첫 실행시만 login화면 나오도록 설정해주는 것
                //isFirstRun을 true로 설정 했다가 첫 실행하고 나면 false로 바까줌
                prefs = getSharedPreferences("Pref",MODE_PRIVATE);
                final boolean isFirstRun = prefs.getBoolean("isFirstRun",true);

                if(isFirstRun) { //첫 실행이면 login화면으로 감, prefs값을 false로 변경
                    intent = new Intent(Intro.this, Login.class);
                    startActivity(intent);
                    prefs.edit().putBoolean("isFirstRun", false).apply();
                    finish();
                }
                else { //첫 실행이 아니면 바로 메인화면으로 감
                    intent = new Intent(Intro.this, MainActivity.class);
                    startActivity(intent);
                  // prefs.edit().putBoolean("isFirstRun", true).apply();
                    finish();
                }
            }
        },1000); //1초동안 인트로 화면 뿌림
    }
}
