package com.example.user.project;

import android.content.Context;
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
import android.widget.CursorAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
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

public class Chart_Frg2_t2 extends Fragment {

    String yy,mm,dd;
    LineChart lineChart;

    ConsumDB conHelper;
    SQLiteDatabase con_db;
    Cursor con_cursor;
    String sql, strconsum;
    int consum;

    TextView textView;
    long now;
    Date date;
    SimpleDateFormat sdf_yy = new SimpleDateFormat("yyyy", Locale.getDefault());
    SimpleDateFormat sdf_mm = new SimpleDateFormat("MM",Locale.getDefault());
    SimpleDateFormat sdf_dd = new SimpleDateFormat("dd",Locale.getDefault());

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.activity_chart__frg2_t2, container, false);

        now = System.currentTimeMillis();
        date = new Date(now);
        yy = sdf_yy.format(date);
        mm = sdf_mm.format(date);
        dd = sdf_dd.format(date);


        conHelper = new ConsumDB(getContext());
        con_db = conHelper.getReadableDatabase();

        textView = (TextView) layout.findViewById(R.id.m_info);
        textView.setText(mm+"월");

        lineChart = (LineChart) layout.findViewById(R.id.chart);
        List<Entry> entries = new ArrayList<>();


        for(int i = 1; i <= Integer.parseInt(dd); i++)
        {
            String j = String.format("%02d",i);
            sql = "select sum(fcal) as cnt from consum where date='"+yy+"-"+mm+"-"+j+"'";
            con_cursor = con_db.rawQuery(sql,null);
            con_cursor.moveToFirst();
            strconsum = con_cursor.getString(con_cursor.getColumnIndex("cnt"));
            if(TextUtils.isEmpty(strconsum))
                strconsum = "0";
            consum = (int)(Double.parseDouble(strconsum));
            entries.add(new Entry(i,consum));
        }

        LineDataSet lineDataSet = new LineDataSet(entries, "칼로리(단위:kcal)");
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

