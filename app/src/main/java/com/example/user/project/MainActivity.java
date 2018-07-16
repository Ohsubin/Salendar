package com.example.user.project;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.text.UnicodeSetSpanner;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.components.MarkerView;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.zip.Inflater;

public class MainActivity extends AppCompatActivity
implements NavigationView.OnNavigationItemSelectedListener{

    public static final String PREFS_NAME = "MyPrefs";
    private Intent intent1,intent2, intent3;
    private String id, gender, age, tall, weight, goal;
    String yy="",mm="",dd="",ee="";
    private int no;
    private SharedPreferences settings;
    Bitmap scaled;
    String suri="",sbit;

    private TextView tvCalendarTitle, main_one, eat_tv, consum_tv, walk_tv;
    private TextView tvSelectedDate,header_id;
    private GridView gvCalendar;
    private ImageView header_image;

    private ArrayList<DayInfo> arrayListDayInfo;
    Calendar mThisMonthCalendar;
    CalendarAdapter mCalendarAdapter;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy년 MM월 dd일(EEE)", Locale.getDefault());
    SimpleDateFormat sdf_yy = new SimpleDateFormat("yyyy",Locale.getDefault());
    SimpleDateFormat sdf_mm = new SimpleDateFormat("MM",Locale.getDefault());
    SimpleDateFormat sdf_dd = new SimpleDateFormat("dd",Locale.getDefault());
    SimpleDateFormat sdf_ee = new SimpleDateFormat("EEE",Locale.getDefault());
    Date selectedDate;

    long now;
    Date date_today;
    SimpleDateFormat sdf_db = new SimpleDateFormat("yyyy-MM-dd");
    String strdate;

    EatDB eatHelper;
    ConsumDB consumHelper;
    OneDB oneHelper;
    WalkDB walkHelper;
    SQLiteDatabase eat_db, consum_db, one_db, walk_db;
    Cursor eat_cursor, consum_cursor, one_cursor, walk_cursor;


    public void setSelectedDate(Date date){
        selectedDate = date;

        if(mCalendarAdapter != null) {
            mCalendarAdapter.selectedDate = date;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //메뉴바
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setSelectedDate(new Date());

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View nav_header = navigationView.getHeaderView(0);

        header_id = (TextView) nav_header.findViewById(R.id.header_id);
        header_image = (ImageView) nav_header.findViewById(R.id.imageView);

        header_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder box = new AlertDialog.Builder(MainActivity.this);
                box.setMessage("사진을 수정하시겠습니까?");
                box.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        intent1 = new Intent(MainActivity.this,Change_image.class);
                        startActivityForResult(intent1,501);
                    }
                });
                box.setNegativeButton("아니요",null);
                box.show();
            }
        });

        //db뿌리기
        eat_tv = (TextView) findViewById(R.id.eat_cal_tv);
        consum_tv = (TextView) findViewById(R.id.consum_cal_tv);
        walk_tv = (TextView) findViewById(R.id.walk_tv);
        main_one = (TextView) findViewById(R.id.main_one);

        now = System.currentTimeMillis();
        date_today = new Date(now);
        strdate = sdf_db.format(date_today);

        eatHelper = new EatDB(this);
        consumHelper = new ConsumDB(this);
        walkHelper = new WalkDB(this);
        oneHelper = new OneDB(this);

        //달력
        tvCalendarTitle = (TextView)findViewById(R.id.tv_calendar_title);
        tvSelectedDate = (TextView)findViewById(R.id.tv_selected_date);

        gvCalendar = (GridView)findViewById(R.id.gv_calendar);
        gvCalendar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setSelectedDate(((DayInfo)view.getTag()).getDate());

                yy = sdf_yy.format(mCalendarAdapter.selectedDate);
                mm = sdf_mm.format(mCalendarAdapter.selectedDate);
                dd = sdf_dd.format(mCalendarAdapter.selectedDate);
                ee = sdf_ee.format(mCalendarAdapter.selectedDate);

                tvSelectedDate.setText(sdf.format(mCalendarAdapter.selectedDate));

                mCalendarAdapter.notifyDataSetChanged();

                strdate = sdf_db.format(mCalendarAdapter.selectedDate);
                db_view();

                intent3 = new Intent(MainActivity.this,Input.class);
                intent3.putExtra("yy",yy);
                intent3.putExtra("mm",mm);
                intent3.putExtra("dd",dd);
                intent3.putExtra("ee",ee);
                startActivityForResult(intent3,500);
            }
        });

        arrayListDayInfo = new ArrayList<>();

        db_view();

        settings = getSharedPreferences(PREFS_NAME,0);
        my_getIntent_fl();

        header_id.setText(id);

        Change_Image();
    }

    public void db_view() {
        //섭취
        eat_db = eatHelper.getReadableDatabase();
        String sql = "select sum(fcal) as cnt from eat where date='"+strdate+"'";
        eat_cursor = eat_db.rawQuery(sql,null);
        eat_cursor.moveToFirst();
        String eat_hap = eat_cursor.getString(eat_cursor.getColumnIndex("cnt"));
        if(TextUtils.isEmpty(eat_hap)==true)
            eat_hap = "0";
        eat_tv.setText("섭취 칼로리 : " + eat_hap);

        //소모
        consum_db = consumHelper.getReadableDatabase();
        sql = "select sum(fcal) as cnt from consum where date='"+strdate+"'";
        consum_cursor = consum_db.rawQuery(sql,null);
        consum_cursor.moveToFirst();
        String consum_hap = consum_cursor.getString(consum_cursor.getColumnIndex("cnt"));
        if(TextUtils.isEmpty(consum_hap)==true)
            consum_hap = "0";
        consum_tv.setText("소모 칼로리 : " + consum_hap);

        //걸은 수
        walk_db = walkHelper.getReadableDatabase();
        sql = "select sum(walk) as cnt from walk where date='"+strdate+"'";
        walk_cursor = walk_db.rawQuery(sql,null);
        walk_cursor.moveToFirst();
        String walk_hap = walk_cursor.getString(walk_cursor.getColumnIndex("cnt"));
        if(TextUtils.isEmpty(walk_hap)==true)
            walk_hap = "0";
        walk_tv.setText("걸은 수 : " + walk_hap);

        //한줄메모
        one_db = oneHelper.getReadableDatabase();
        sql = "select one ,sum(one) as cnt from one where date='"+strdate+"'";
        one_cursor = one_db.rawQuery(sql,null);
        one_cursor.moveToFirst();
        String one_meno = one_cursor.getString(one_cursor.getColumnIndex("one"));
        if(TextUtils.isEmpty(one_meno)==true)
            one_meno = "한줄 메모를 입력하세요.";
        main_one.setText("한줄 메모 : " + one_meno);

    }

    public void my_getIntent_fl() {
        intent1 = getIntent();
        no = intent1.getIntExtra("no",0);
        if(no==1) {
            id = intent1.getStringExtra("id");
            tall = intent1.getStringExtra("tall");
            weight = intent1.getStringExtra("weight");
            gender = intent1.getStringExtra("gender");
            age = intent1.getStringExtra("age");
            goal = intent1.getStringExtra("goal");
            sbit = settings.getString("sbit","");
        }
        else {
            id = settings.getString("id","");
            tall = settings.getString("tall","");
            weight = settings.getString("weight","");
            gender = settings.getString("gender","");
            age = settings.getString("age","");
            goal = settings.getString("goal","");
            sbit = settings.getString("sbit","");
        }
    }

    public void Change_Image() {
        if(sbit.equals("")) {

        }
        else {
            byte[] decodedByteArray = Base64.decode(sbit,Base64.NO_WRAP);
            Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.length);
            header_image.setImageBitmap(decodedBitmap);
        }

    }
    @Override
    protected void onStop() {
        super.onStop();

        SharedPreferences settings = getSharedPreferences(PREFS_NAME,0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("id",id);
        editor.putString("tall",tall);
        editor.putString("weight",weight);
        editor.putString("gender",gender);
        editor.putString("age",age);
        editor.putString("goal",goal);
        editor.putString("sbit",sbit);
        editor.commit();

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.menu_myinfo) {
            my_putIntent_fi();
        } else if (id == R.id.menu_manbogi) {
            intent2 = new Intent(MainActivity.this,Manbogi.class);
            startActivity(intent2);
        } else if (id == R.id.menu_water) {
           intent2 = new Intent(MainActivity.this,Water.class);
           startActivity(intent2);
        } else if (id == R.id.menu_grape) {
            intent2 = new Intent(MainActivity.this,Chart.class);
            startActivity(intent2);
        }else if(id == R.id.menu_photo) {
            intent2 = new Intent(MainActivity.this,Photo.class);
            startActivity(intent2);
        } else if(id == R.id.menu_youtube) {
            intent2 = new Intent(MainActivity.this,Video.class);
            startActivity(intent2);
        } else if(id == R.id.menu_developer) {
            intent2 = new Intent(MainActivity.this,Developer.class);
            startActivity(intent2);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void my_putIntent_fi() {
        intent2 = new Intent(MainActivity.this,Info.class);
        intent2.putExtra("id",id);
        intent2.putExtra("tall",tall);
        intent2.putExtra("weight",weight);
        intent2.putExtra("gender",gender);
        intent2.putExtra("age",age);
        intent2.putExtra("goal",goal);
        startActivityForResult(intent2,100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 100) {
            if(resultCode == 8) {
                id = data.getStringExtra("id");
                age = data.getStringExtra("age");
                gender = data.getStringExtra("gender");
                tall = data.getStringExtra("tall");
                weight = data.getStringExtra("weight");
                goal = data.getStringExtra("goal");
            }
        }
        else if(requestCode == 501) {
            if(resultCode == 8 ) {
                suri = data.getStringExtra("suri");
                Uri uri = Uri.parse(suri);
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                    //이미지가 한계이상(?) 크면 불러 오지 못하므로 사이즈를 줄여 준다.
                    int nh = (int) (bitmap.getHeight() * (1024.0 / bitmap.getWidth()));
                    scaled = Bitmap.createScaledBitmap(bitmap, 1024, nh, true);
                }catch (FileNotFoundException e) {
                    e.printStackTrace();
                }catch (IOException e) {
                    e.printStackTrace();
                }
                sbit = getBase64String(scaled);
                header_image.setImageBitmap(scaled);
            }
        }
    }

    public String getBase64String(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imageBytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.NO_WRAP);
    }

    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_previous_calendar :
                mThisMonthCalendar.add(Calendar.MONTH, -1);
                getCalendar(mThisMonthCalendar.getTime());
                break;
            case R.id.btn_next_calendar :
                mThisMonthCalendar.add(Calendar.MONTH, +1);
                getCalendar(mThisMonthCalendar.getTime());
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        mThisMonthCalendar = Calendar.getInstance();
        getCalendar(mThisMonthCalendar.getTime());

        header_id.setText(id);
        db_view();
    }

    private void getCalendar(Date dateForCurrentMonth){
        int dayOfWeek;
        int thisMonthLastDay;

        arrayListDayInfo.clear();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateForCurrentMonth);

        calendar.set(Calendar.DATE, 1);//1일로 변경
        dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);//1일의 요일 구하기
        Log.d("CalendarTest", "dayOfWeek = " + dayOfWeek+"");

        if(dayOfWeek == Calendar.SUNDAY){ //현재 달의 1일이 무슨 요일인지 검사
            dayOfWeek += 7;
        }

        thisMonthLastDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        setCalendarTitle();

        DayInfo day;

        calendar.add(Calendar.DATE, -1*(dayOfWeek-1)); //현재 달력화면에서 보이는 지난달의 시작일
        for(int i=0; i<dayOfWeek-1; i++){
            day = new DayInfo();
            day.setDate(calendar.getTime());
            day.setInMonth(false);
            arrayListDayInfo.add(day);

            calendar.add(Calendar.DATE, +1);
        }

        for(int i=1; i <= thisMonthLastDay; i++){
            day = new DayInfo();
            day.setDate(calendar.getTime());
            day.setInMonth(true);
            arrayListDayInfo.add(day);

            calendar.add(Calendar.DATE, +1);
        }

        for(int i=1; i<42-(thisMonthLastDay+dayOfWeek-1)+1; i++) {
            day = new DayInfo();
            day.setDate(calendar.getTime());
            day.setInMonth(false);
            arrayListDayInfo.add(day);

            calendar.add(Calendar.DATE, +1);
        }

        mCalendarAdapter = new CalendarAdapter(arrayListDayInfo, selectedDate);
        gvCalendar.setAdapter(mCalendarAdapter);

        tvSelectedDate.setText(sdf.format(selectedDate));
    }

    private void setCalendarTitle(){
        StringBuilder sb = new StringBuilder();

        sb.append(mThisMonthCalendar.get(Calendar.YEAR))
                .append("년 ")
                .append((mThisMonthCalendar.get(Calendar.MONTH) + 1))
                .append("월");
        tvCalendarTitle.setText(sb.toString());
    }

}
