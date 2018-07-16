package com.example.user.project;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;


public class Chart extends AppCompatActivity {
    PageAdapter adapter;
    ViewPager pager;
    Button btn1, btn2, btn3;
    Chart_Frg1 chart_frg1;
    Chart_Frg2 chart_frg2;
    Chart_Frg3 chart_frg3;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        ActionBar ab = getSupportActionBar();
        ab.hide();

        chart_frg1 = new Chart_Frg1();
        chart_frg2 = new Chart_Frg2();
        chart_frg3 = new Chart_Frg3();

        adapter = new PageAdapter(getSupportFragmentManager());
        pager = (ViewPager)findViewById(R.id.pager);
        pager.setAdapter(adapter);
        pager.setCurrentItem(0);

        btn1 = (Button)findViewById(R.id.btn1);
        btn1.setOnClickListener(mClick);
        btn2 = (Button)findViewById(R.id.btn2);
        btn2.setOnClickListener(mClick);
        btn3 = (Button)findViewById(R.id.btn3);
        btn3.setOnClickListener(mClick);
        btn1.setTextColor(Color.RED);

        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        btn1.setTextColor(Color.RED);
                        btn2.setTextColor(Color.WHITE);
                        btn3.setTextColor(Color.WHITE);
                        break;
                    case 1:
                        btn1.setTextColor(Color.WHITE);
                        btn2.setTextColor(Color.RED);
                        btn3.setTextColor(Color.WHITE);
                        break;
                    case 2:
                        btn1.setTextColor(Color.WHITE);
                        btn2.setTextColor(Color.WHITE);
                        btn3.setTextColor(Color.RED);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    View.OnClickListener mClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn1:
                    pager.setCurrentItem(0);
                    break;

                case R.id.btn2:
                    pager.setCurrentItem(1);
                    break;

                case R.id.btn3:
                    pager.setCurrentItem(2);
                    break;
            }
        }
    };
    class PageAdapter extends FragmentStatePagerAdapter{

        public PageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            switch (position){
                case 0:
                    return chart_frg1;
                case 1:
                    return chart_frg2;
                case 2:
                    return chart_frg3;
            }
            return null;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public int getItemPosition(@NonNull Object object) {

            return POSITION_NONE;
        }
    }
}