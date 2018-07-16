package com.example.user.project;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Chart_Frg3 extends Fragment {
    String yy,mm,dd;
    LineChart lineChart;

    WalkDB walkHelper;
    SQLiteDatabase walk_db;
    Cursor walk_cursor;
    String sql, strwalk;
    int walk;

    TextView textView;
    long now;
    Date date;
    SimpleDateFormat sdf_yy = new SimpleDateFormat("yyyy", Locale.getDefault());
    SimpleDateFormat sdf_mm = new SimpleDateFormat("MM",Locale.getDefault());
    SimpleDateFormat sdf_dd = new SimpleDateFormat("dd",Locale.getDefault());

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LinearLayout layout=(LinearLayout)inflater.inflate(R.layout.activity_chart__frg3,container,false);

        now = System.currentTimeMillis();
        date = new Date(now);
        yy = sdf_yy.format(date);
        mm = sdf_mm.format(date);
        dd = sdf_dd.format(date);

        walkHelper = new WalkDB(getContext());
        walk_db = walkHelper.getReadableDatabase();

        textView = (TextView) layout.findViewById(R.id.m_info);
        textView.setText(mm+"월");

        lineChart = (LineChart) layout.findViewById(R.id.chart);
        List<Entry> entries = new ArrayList<>();

        for(int i = 1; i <= Integer.parseInt(dd); i++)
        {
            String j = String.format("%02d",i);
            sql = "select walk, sum(date) as cnt from walk where date='"+yy+"-"+mm+"-"+j+"'";
            walk_cursor = walk_db.rawQuery(sql,null);
            walk_cursor.moveToFirst();
            strwalk = walk_cursor.getString(walk_cursor.getColumnIndex("walk"));
            if(TextUtils.isEmpty(strwalk))
                strwalk = "0";
            walk = Integer.parseInt(strwalk);
            entries.add(new Entry(i,walk));
        }

        LineDataSet lineDataSet = new LineDataSet(entries, "걸음 수(단위:보)");
        lineDataSet.setLineWidth(2);
        lineDataSet.setCircleRadius(6);
        lineDataSet.setCircleColor(Color.parseColor("#FFA1B4DC"));
        lineDataSet.setCircleColorHole(Color.BLUE);
        lineDataSet.setColor(Color.parseColor("#FFA1B4DC"));
        lineDataSet.setDrawCircleHole(true);
        lineDataSet.setDrawCircles(true);
        lineDataSet.setDrawHorizontalHighlightIndicator(false);
        lineDataSet.setDrawHighlightIndicators(false);
        lineDataSet.setDrawValues(false);

        LineData lineData = new LineData(lineDataSet);
        lineChart.setData(lineData);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(Color.BLACK);
        xAxis.enableGridDashedLine(8, 24, 0);

        YAxis yLAxis = lineChart.getAxisLeft();
        yLAxis.setTextColor(Color.BLACK);

        YAxis yRAxis = lineChart.getAxisRight();
        yRAxis.setDrawLabels(false);
        yRAxis.setDrawAxisLine(false);
        yRAxis.setDrawGridLines(false);

        Description description = new Description();
        description.setText("");

        lineChart.setDoubleTapToZoomEnabled(false);
        lineChart.setDrawGridBackground(false);
        lineChart.setDescription(description);
        lineChart.animateY(2000, Easing.EasingOption.EaseInCubic);
        lineChart.invalidate();

        Chart_Maker marker = new Chart_Maker(getContext(),R.layout.chart_marker);
        marker.setChartView(lineChart);
        lineChart.setMarker(marker);

        return layout;
    }
}
