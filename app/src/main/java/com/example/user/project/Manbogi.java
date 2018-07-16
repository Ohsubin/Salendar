package com.example.user.project;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;


public class Manbogi extends Activity {
    public static int Step = 0;
    Intent intentMyService;
    BroadcastReceiver receiver;
    boolean flag = true;
    Toast toast;
    TextView CountText,btnStopService, somotext, namtext, walktext;
    String serviceData, str, saveData, savesomo, savenam, savewalk;
    double somo;
    String walk_str;
    int su;
    double walk;
    int nam;

    public static final String PREFS_NAME = "MyPrefs";
    private SharedPreferences settings;

    WalkDB walk_Helper;
    SQLiteDatabase walk_db;
    long now;
    Date date;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    String strdate, sql;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manbogi);

        walk_Helper = new WalkDB(this);
        walk_db = walk_Helper.getWritableDatabase();

        now = System.currentTimeMillis();
        date = new Date(now);
        strdate = sdf.format(date);

        intentMyService = new Intent(this,MyIntentService.class);
        //서비스 등록
        receiver = new MyMainLocalRecever();

        CountText = (TextView)findViewById(R.id.countTV);
        somotext=(TextView)findViewById(R.id.somotext);
        namtext = (TextView)findViewById(R.id.namtext);
        walktext = (TextView) findViewById(R.id.walktext);


        btnStopService = (TextView) findViewById(R.id.btnStopService);
        //서비스 시작,중지 버튼

        btnStopService.setOnClickListener(new OnClickListener() {

            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View v) {

                if(flag)
                {
                    Intent intent=new Intent(Manbogi.this,MyIntentService.class);
                    intent.setAction("startForeground");
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        startForegroundService(intent);
                    }else {
                        stopService(intent);
                    }
                    btnStopService.setBackgroundResource(R.drawable.icon_footprint_go);
                    try{

                        IntentFilter mainFilter = new IntentFilter("com.androday.test.step");

                        registerReceiver(receiver, mainFilter);

                        startService(intentMyService);
                        //txtMsg.setText("After stoping Service:\n"+service.getClassName());
                        Toast.makeText(getApplicationContext(), "시작", 1).show();
                    }
                    catch (Exception e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), 1).show();
                    }
                }

                else
                {

                    btnStopService.setBackgroundResource(R.drawable.icon_footprint_stop);

                    try{

                        unregisterReceiver(receiver);

                        stopService(intentMyService);

                        Toast.makeText(getApplicationContext(), "정지", 1).show();
                        //txtMsg.setText("After stoping Service:\n"+service.getClassName());
                    }
                    catch (Exception e) {

                        Toast.makeText(getApplicationContext(), e.getMessage(), 1).show();
                    }
                }

                flag = !flag;

            }
        });

        settings = getSharedPreferences(PREFS_NAME,0);
        saveData = settings.getString("count","0");
        savesomo = settings.getString("somo","0");
        savenam = settings.getString("nam","0");
        savewalk = settings.getString("walk","0");
        CountText.setText(saveData);
        somotext.setText(savesomo+"cal");
        namtext.setText(savenam);
        walktext.setText(savewalk+"m");
    }


    class MyMainLocalRecever extends BroadcastReceiver {

        @SuppressLint("WrongConstant")
        @Override
        public void onReceive(Context context, Intent intent) {
            serviceData = intent.getStringExtra("serviceData");
            su=Integer.parseInt(serviceData);
            walk = su * 0.665;
            nam = 15000 - su;
            somo =  0.033 * su; //소모칼로리(걸음수) 계산
            str = String.format("%.1f", somo);//소수점
            walk_str = String.format("%.1f",walk);

            savesomo = str;
            savenam = String.valueOf(nam);
            savewalk = walk_str;
            saveData = serviceData;

            sql = "insert into walk(walk, date) values(";
            sql += "'"+saveData+"',";
            sql += "'"+strdate+"')";
            walk_db.execSQL(sql);

            somotext.setText(str+"cal");
            CountText.setText(serviceData);
            namtext.setText(String.valueOf(nam));
            walktext.setText(walk_str+"m");

        }

    }
    @Override
    protected void onPause() {
        super.onPause();

        settings = getSharedPreferences(PREFS_NAME,0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("count",saveData);
        editor.putString("somo",savesomo);
        editor.putString("nam",savenam);
        editor.putString("walk",savewalk);
        editor.commit();
    }

}