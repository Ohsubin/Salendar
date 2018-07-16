package com.example.user.project;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CalendarAdapter extends BaseAdapter{
    private ArrayList<DayInfo> arrayListDayInfo;
    public Date selectedDate;

    OneDB oneHelper;
    SQLiteDatabase one_db;
    Cursor one_cursor;

    public CalendarAdapter(ArrayList<DayInfo> arrayLIstDayInfo, Date date) {
        this.arrayListDayInfo = arrayLIstDayInfo;
        this.selectedDate = date;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return arrayListDayInfo.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return arrayListDayInfo.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        DayInfo day = arrayListDayInfo.get(position);

        if(convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.day, parent, false);
        }

        if(day != null){
            String mm="0",dd,yy, strdate;
            TextView tvDay = convertView.findViewById(R.id.day_cell_tv_day);
            TextView tvOne = convertView.findViewById(R.id.tvOne);
            RelativeLayout layout = convertView.findViewById(R.id.day_cell_ll_background);
            tvDay.setText(day.getDay());

            Log.d("test",day.getDate().toString());
            //한줄 메모 등록하기 시작
            if(day.getDate().toString().substring(4,7).equals("Jan"))
                mm="01";
            else if(day.getDate().toString().substring(4,7).equals("Feb"))
                mm="02";
            else if(day.getDate().toString().substring(4,7).equals("Mar"))
                mm="03";
            else if(day.getDate().toString().substring(4,7).equals("Apr"))
                mm="04";
            else if(day.getDate().toString().substring(4,7).equals("May"))
                mm="05";
            else if(day.getDate().toString().substring(4,7).equals("Jun"))
                mm="06";
            else if(day.getDate().toString().substring(4,7).equals("Jul"))
                mm="07";
            else if(day.getDate().toString().substring(4,7).equals("Aug"))
                mm="08";
            else if(day.getDate().toString().substring(4,7).equals("Sep"))
                mm="09";
            else if(day.getDate().toString().substring(4,7).equals("Oct"))
                mm="10";
            else if(day.getDate().toString().substring(4,7).equals("Nov"))
                mm="11";
            else if(day.getDate().toString().substring(4,7).equals("Dec"))
                mm="12";

            dd=day.getDate().toString().substring(8,10);
            yy=day.getDate().toString().substring(30,34);
            strdate = yy+"-"+mm+"-"+dd;

            Log.d("test",day.getDate().toString());
            Log.d("test",yy);
            oneHelper = new OneDB(parent.getContext());
            one_db = oneHelper.getReadableDatabase();
            String sql = "select one ,sum(one) as cnt from one where date='"+strdate+"'";
            Log.d("test",sql);
            one_cursor = one_db.rawQuery(sql,null);
            one_cursor.moveToFirst();
            String one_meno = one_cursor.getString(one_cursor.getColumnIndex("one"));
            if(TextUtils.isEmpty(one_meno)==true)
                one_meno="";
            tvOne.setText(one_meno);
            //한줄 메모 등록 끝

            //선택된 날짜 디자인 바꾸기 시작
            ImageView ivSelected = convertView.findViewById(R.id.iv_selected);
            if(day.isSameDay(selectedDate)){
                ivSelected.setVisibility(View.VISIBLE);
            }else{
                ivSelected.setVisibility(View.INVISIBLE);
            }
            //선택된 날짜 디자인 바꾸기 끝

            //토요일 일요일에 글자 색 바꾸고 달력 디자인 바꾸기 시작
            if(day.isInMonth()){
                if((position%7 + 1) == Calendar.SUNDAY){
                    tvDay.setTextColor(Color.RED);
                   layout.setBackgroundResource(R.drawable.calback_wr);
                }else if((position%7 + 1) == Calendar.SATURDAY){
                    tvDay.setTextColor(Color.BLUE);
                    layout.setBackgroundResource(R.drawable.calback_bw);
                }else{
                    tvDay.setTextColor(Color.WHITE);
                }
            }else if((position%7 + 1 ) == Calendar.SUNDAY) {
                layout.setBackgroundResource(R.drawable.calback_wr);
                tvDay.setTextColor(Color.GRAY);
                tvOne.setTextColor(Color.GRAY);
            }else if((position%7 + 1) == Calendar.SATURDAY) {
                layout.setBackgroundResource(R.drawable.calback_bw);
                tvDay.setTextColor(Color.GRAY);
                tvOne.setTextColor(Color.GRAY);
            } else {
                tvDay.setTextColor(Color.GRAY);
                tvOne.setTextColor(Color.GRAY);
            }
        }
        convertView.setTag(day);
        //디자인 바꾸기 끝

        return convertView;
    }

}
