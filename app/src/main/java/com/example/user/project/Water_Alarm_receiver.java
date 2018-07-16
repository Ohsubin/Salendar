package com.example.user.project;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

public class Water_Alarm_receiver extends BroadcastReceiver {
    static  final  int NAPNOTI = 1;
    NotificationManager manager;
    @Override
    public void onReceive(Context context, Intent intent) {
        manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent intent1 = new Intent(context,Water.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intent1,0);

        NotificationCompat.Builder builder=new NotificationCompat.Builder(context,"sal");
        builder.setTicker("물을 먹으세요")
                        .setContentTitle("물 마실 시간!")
                        .setContentText("물 물 물")
                        .setSubText("물을 드시오~")
                        .setSmallIcon(R.mipmap.ic_launcher_icon)
                        .setContentIntent(pendingIntent)
                        .build();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            manager.createNotificationChannel(new NotificationChannel("sal","기본채널",NotificationManager.IMPORTANCE_DEFAULT));
        }
        manager.notify(Water_Alarm_receiver.NAPNOTI,builder.build());
    }

}
