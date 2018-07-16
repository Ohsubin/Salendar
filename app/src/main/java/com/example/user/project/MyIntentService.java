package com.example.user.project;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;


import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

class MyIntentService extends Service implements SensorEventListener  {

    public static class values {

        public static int Step = 0;
    }


    int count = values.Step;

    private long lastTime;
    private float speed;
    private float lastX;
    private float lastY;
    private float lastZ;


    private float x, y, z;
    private static final int SHAKE_THRESHOLD = 800;

    private static final int DATA_X = SensorManager.DATA_X;
    private static final int DATA_Y = SensorManager.DATA_Y;
    private static final int DATA_Z = SensorManager.DATA_Z;


    private SensorManager sensorManager;
    private Sensor accelerormeterSensor;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("MyServiceIntent","Service is Create");

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerormeterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        Log.i("MyServiceIntent","Service is started");
        if ("startForeground".equals(intent.getAction())) {
            stratForgroundService();
        } else if (accelerormeterSensor != null)
            sensorManager.registerListener(this, accelerormeterSensor,
                    SensorManager.SENSOR_DELAY_GAME);


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("MyServiceIntent","Service is destroy");

        if (sensorManager != null)
            sensorManager.unregisterListener(this);

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            long currentTime = System.currentTimeMillis();
            long gabOfTime = (currentTime - lastTime);


            if (gabOfTime > 100) {
                lastTime = currentTime;

                x = event.values[SensorManager.DATA_X];
                y = event.values[SensorManager.DATA_Y];
                z = event.values[SensorManager.DATA_Z];


                speed = Math.abs(x + y + z - lastX - lastY - lastZ) / gabOfTime * 7000;


                if (speed > SHAKE_THRESHOLD) {

                    Log.e("Step!", "SHAKE");

                    Intent myFilteredResponse = new Intent("com.androday.test.step");

                    values.Step = count++;

                    String msg = values.Step + "" ;
                    myFilteredResponse.putExtra("serviceData", msg);

                    sendBroadcast(myFilteredResponse);

                }
                lastX = event.values[DATA_X];
                lastY = event.values[DATA_Y];
                lastZ = event.values[DATA_Z];
            }
        }

    }
    private void stratForgroundService(){
        NotificationCompat.Builder builder=new NotificationCompat.Builder(this,"default");
        builder.setSmallIcon(R.mipmap.ic_launcher);//아이콘생성
        builder.setContentTitle("살린더-만보기");//제목
        builder.setContentText("실행중!");//내용

        //알림을 클릭했을때 액티비티 수행
        Intent notificationIntent= new Intent(this,MainActivity.class);
        PendingIntent pendingIntent= PendingIntent.getActivity(this,0,notificationIntent,0);
        builder.setContentIntent(pendingIntent);

        //오레오 이상에서 사용할수있게 알림채널 등록
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager manager=(NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.createNotificationChannel(new NotificationChannel("default","기본채널",NotificationManager.IMPORTANCE_DEFAULT));
        }
        startForeground(1,builder.build());//실행 (오레오에서는 실행불가)
    }

}
