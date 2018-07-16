package com.example.user.project;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class Input extends AppCompatActivity {
    Intent intent, intent2;
    String yy,mm,dd,ee, sql, string, strdate;
    TextView dayInfo;
    EditText et_eat, et_consum, et_oneline;
    ListView list;

    //SQLite 사용 시 필요한 선언들
    FoodDB food_helper;
    EatDB eat_Helper;
    ConsumDB consum_Helper;
    OneDB one_Helper;
    SQLiteDatabase food_db, eat_db, one_db, consum_db;
    Cursor food_cursor;
    SimpleCursorAdapter food_adapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);

        ActionBar ab = getSupportActionBar();
        ab.hide();

        //layout 정의
        et_consum = (EditText) findViewById(R.id.et_consum);
        dayInfo = (TextView) findViewById(R.id.day_info);
        et_oneline = (EditText) findViewById(R.id.et_oneline);
        et_eat=(EditText)findViewById(R.id.et_eat);
        list=(ListView)findViewById(R.id.list);

        //main에서 날짜 받아오기
        getIntent_main();

        //DB 사용
        food_helper=new FoodDB(this);
        food_db=food_helper.getWritableDatabase();

        eat_Helper = new EatDB(this);
        eat_db = eat_Helper.getWritableDatabase();

        consum_Helper = new ConsumDB(this);
        consum_db = consum_Helper.getWritableDatabase();

        one_Helper = new OneDB(this);
        one_db = one_Helper.getWritableDatabase();

        //날짜 보여주기
        dayInfo.setText(yy+"년 "+mm+"월 "+dd+"일 ("+ee+")");

        //db에 date필드 저장할때 메인에서 선택된 날짜 입력하기 위해
        strdate = yy+"-"+mm+"-"+dd;

        //리스트 뷰 클릭 이벤트
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //리스트 뷰 클릭 시 칼로리값만 받아서 EatDB에 저장
                sql = "insert into eat(fcal, date) values(";
                sql += "'"+food_cursor.getString(2)+"',";
                sql += "'"+strdate+"')";
                eat_db.execSQL(sql);
                et_eat.setText("");
            }
        });
    }

    //메인에서 날짜 받아오기
    public void getIntent_main() {
        intent = getIntent();
        yy = intent.getStringExtra("yy");
        mm = intent.getStringExtra("mm");
        dd = intent.getStringExtra("dd");
        ee = intent.getStringExtra("ee");
    }

    //입력, 검색 버튼 클릭
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_eat: //검색
                string=et_eat.getText().toString();
                sql="select * from cal where fname like '%" + string + "%' order by fname";

                System.out.println("query=" + sql);
                food_cursor=food_db.rawQuery(sql, null);
                food_adapter=new SimpleCursorAdapter(list.getContext(), R.layout.item, food_cursor,
                        new String[] {"fname", "fcal", "fgram"},
                        new int[] {R.id.fname, R.id.fcal, R.id.fgram});
                list.setAdapter(food_adapter);
                break;
            case R.id.btn_consum : //소모량 db에 저장
                string = et_consum.getText().toString();
                sql = "insert into consum(fcal, date) values(";
                sql += "'"+string+"',";
                sql += "'"+strdate+"')";
                consum_db.execSQL(sql);
                et_consum.setText("");
                break;
            case R.id.btn_oneline : //한줄메모 db에 저장
                string = et_oneline.getText().toString();
                sql = "insert into one(one, date) values(";
                sql += "'"+string+"',";
                sql += "'"+strdate+"')";
                one_db.execSQL(sql);
                et_oneline.setText("");
                break;
        }
    }
}
