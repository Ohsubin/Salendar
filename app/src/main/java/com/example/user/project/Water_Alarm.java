package com.example.user.project;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;


public class Water_Alarm extends AppCompatActivity {
    Spinner spinner;
    TimePicker start_picker;
    int tap, start_hour, start_minute;

    AlarmManager alarmManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water__alarm);

        ActionBar ab = getSupportActionBar();
        ab.hide();

        spinner = (Spinner) findViewById(R.id.spinner);
        start_picker = (TimePicker) findViewById(R.id.start_picker);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.time_array,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(0);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0)
                    tap = Integer.parseInt(parent.getItemAtPosition(position).toString().substring(0,2));
                else
                    tap = parent.getItemAtPosition(position).toString().charAt(0) - '0';

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        start_picker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                start_hour = hourOfDay;
                start_minute = minute;
            }
        });

    }

    public void onClick(View v) {
        Calendar start_c = Calendar.getInstance();
        start_c.set(Calendar.HOUR_OF_DAY,start_hour);
        start_c.set(Calendar.MINUTE,start_minute);

        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(this,Water_Alarm_receiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);

        if (tap == 30)
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,start_c.getTimeInMillis(),AlarmManager.INTERVAL_HALF_HOUR,pendingIntent);
        else
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,start_c.getTimeInMillis(),AlarmManager.INTERVAL_HOUR*tap,pendingIntent);
        Toast.makeText(Water_Alarm.this, "알람이 설정되었습니다.",Toast.LENGTH_SHORT).show();
    }
}
