package com.example.user.project;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Water extends AppCompatActivity {

    public static final String PREFS_NAME = "MyPrefs";
    SharedPreferences settings;

    int n1,n2,n3,n4,n5,n6,n7,n8;
    ImageView w1,w2,w3,w4,w5,w6,w7,w8;

    long now;
    Date date;
    SimpleDateFormat sdfNow;
    String formatDate1="", formatDate2="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water);

        ActionBar ab = getSupportActionBar();
        ab.hide();

        now = System.currentTimeMillis();
        date = new Date(now);
        sdfNow = new SimpleDateFormat("yyyyMMdd");
        formatDate2 = sdfNow.format(date);

        ImageView_Set();
        Settings_Set();
        if(!formatDate1.equals(formatDate2))  {
            n1=0;n2=0;n3=0;n4=0;n5=0;n6=0;n7=0;n8=0;
        }

        Setting_ImageView();
    }

    public void ImageView_Set() {
        w1 = (ImageView) findViewById(R.id.water1);
        w2 = (ImageView) findViewById(R.id.water2);
        w3 = (ImageView) findViewById(R.id.water3);
        w4 = (ImageView) findViewById(R.id.water4);
        w5 = (ImageView) findViewById(R.id.water5);
        w6 = (ImageView) findViewById(R.id.water6);
        w7 = (ImageView) findViewById(R.id.water7);
        w8 = (ImageView) findViewById(R.id.water8);
    }

    public void Settings_Set() {
            settings = getSharedPreferences(PREFS_NAME, 0);
            n1 = settings.getInt("n1", 0);
            n2 = settings.getInt("n2", 0);
            n3 = settings.getInt("n3", 0);
            n4 = settings.getInt("n4", 0);
            n5 = settings.getInt("n5", 0);
            n6 = settings.getInt("n6", 0);
            n7 = settings.getInt("n7", 0);
            n8 = settings.getInt("n8", 0);
        formatDate1 = settings.getString("formatDate","");
    }

    public void Setting_ImageView() {
        if(n1==1)
            w1.setImageResource(R.drawable.icon_water3);
        if(n2==1)
            w2.setImageResource(R.drawable.icon_water3);
        if(n3==1)
            w3.setImageResource(R.drawable.icon_water3);
        if(n4==1)
            w4.setImageResource(R.drawable.icon_water3);
        if(n5==1)
            w5.setImageResource(R.drawable.icon_water3);
        if(n6==1)
            w6.setImageResource(R.drawable.icon_water3);
        if(n7==1)
            w7.setImageResource(R.drawable.icon_water3);
        if(n8==1)
            w8.setImageResource(R.drawable.icon_water3);
    }

    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.btn1 :
                if (n1 == 0) {
                 w1.setImageResource(R.drawable.icon_water3);
                 n1 = 1;
                 settings.edit().putInt("n1", 1).apply();
               } else if (n1 == 1 && n2 == 0) {
                 w2.setImageResource(R.drawable.icon_water3);
                 n2 = 1;
                 settings.edit().putInt("n2", 1).apply();
             } else if (n1 == 1 && n2 == 1 && n3 == 0) {
                 w3.setImageResource(R.drawable.icon_water3);
                 n3 = 1;
                 settings.edit().putInt("n3", 1).apply();
             } else if (n1 == 1 && n2 == 1 && n3 == 1 && n4 == 0) {
                 w4.setImageResource(R.drawable.icon_water3);
                 n4 = 1;
                 settings.edit().putInt("n4", 1).apply();
             } else if (n1 == 1 && n2 == 1 && n3 == 1 && n4 == 1 && n5 == 0) {
                 w5.setImageResource(R.drawable.icon_water3);
                 n5 = 1;
                 settings.edit().putInt("n5", 1).apply();
             } else if (n1 == 1 && n2 == 1 && n3 == 1 && n4 == 1 && n5 == 1 && n6 == 0) {
                 w6.setImageResource(R.drawable.icon_water3);
                 n6 = 1;
                 settings.edit().putInt("n6", 1).apply();
             } else if (n1 == 1 && n2 == 1 && n3 == 1 && n4 == 1 && n5 == 1 && n6 == 1 && n7 == 0) {
                 w7.setImageResource(R.drawable.icon_water3);
                 n7 = 1;
                 settings.edit().putInt("n7", 1).apply();
             } else if (n1 == 1 && n2 == 1 && n3 == 1 && n4 == 1 && n5 == 1 && n6 == 1 && n7 == 1 && n8 == 0) {
                 w8.setImageResource(R.drawable.icon_water3);
                 n8 = 1;
                settings.edit().putInt("n8", 1).apply();
             } else
                 Toast.makeText(Water.this, "2L를 다 마셨습니다.", Toast.LENGTH_LONG).show();
                break;

            case R.id.btn2 :
                if (n1 == 1 && n2 == 1 && n3 == 1 && n4 == 1 && n5 == 1 && n6 == 1 && n7 == 1 && n8 == 1) {
                    w8.setImageResource(R.drawable.icon_water4);
                    n8 = 0;
                    settings.edit().putInt("n8", 0).apply();
                }else if (n1 == 1 && n2 == 1 && n3 == 1 && n4 == 1 && n5 == 1 && n6 == 1 && n7 == 1 && n8 !=1) {
                    w7.setImageResource(R.drawable.icon_water4);
                    n7 = 0;
                    settings.edit().putInt("n7", 0).apply();
                }else if (n1 == 1 && n2 == 1 && n3 == 1 && n4 == 1 && n5 == 1 && n6 == 1 && n7 != 1 && n8 !=1) {
                    w6.setImageResource(R.drawable.icon_water4);
                    n6 = 0;
                    settings.edit().putInt("n6", 0).apply();
                }
                else if (n1 == 1 && n2 == 1 && n3 == 1 && n4 == 1 && n5 == 1 && n6 != 1 && n7 != 1 && n8 !=1) {
                    w5.setImageResource(R.drawable.icon_water4);
                    n5 = 0;
                    settings.edit().putInt("n5", 0).apply();
                }
                else if (n1 == 1 && n2 == 1 && n3 == 1 && n4 == 1 && n5 != 1 && n6 != 1 && n7 != 1 && n8 !=1) {
                    w4.setImageResource(R.drawable.icon_water4);
                    n4 = 0;
                    settings.edit().putInt("n4", 0).apply();
                }
                else if (n1 == 1 && n2 == 1 && n3 == 1 && n4 != 1 && n5 != 1 && n6 != 1 && n7 != 1 && n8 !=1) {
                    w3.setImageResource(R.drawable.icon_water4);
                    n3 = 0;
                    settings.edit().putInt("n3", 0).apply();
                }
                else if (n1 == 1 && n2 == 1 && n3 != 1 && n4 != 1 && n5 != 1 && n6 != 1 && n7 != 1 && n8 !=1) {
                    w2.setImageResource(R.drawable.icon_water4);
                    n2 = 0;
                    settings.edit().putInt("n2", 0).apply();
                }
                else if (n1 == 1 && n2 != 1 && n3 != 1 && n4 != 1 && n5 != 1 && n6 != 1 && n7 != 1 && n8 !=1) {
                    w1.setImageResource(R.drawable.icon_water4);
                    n1 = 0;
                    settings.edit().putInt("n1", 0).apply();
                }
                else
                    Toast.makeText(Water.this,"마신 물이 없습니다.",Toast.LENGTH_LONG).show();
                break;
            case R.id.set_Alarm :
                Intent intent = new Intent(Water.this, Water_Alarm.class);
                startActivity(intent);
                break;
            case R.id.reset_Alarm :
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                Intent intent1 = new Intent(this,Water_Alarm_receiver.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(this,0,intent1,PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager.cancel(pendingIntent);
                Toast.makeText(this,"알람이 해제되었습니다.",Toast.LENGTH_SHORT).show();
                break;

        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        SharedPreferences settings = getSharedPreferences(PREFS_NAME,0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("n1",n1);
        editor.putInt("n2",n2);
        editor.putInt("n3",n3);
        editor.putInt("n4",n4);
        editor.putInt("n5",n5);
        editor.putInt("n6",n6);
        editor.putInt("n7",n7);
        editor.putInt("n8",n8);
        editor.putString("formatDate",formatDate2);
        editor.commit();

    }
}
