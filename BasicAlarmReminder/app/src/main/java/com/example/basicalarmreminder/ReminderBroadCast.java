package com.example.basicalarmreminder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class ReminderBroadCast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "notifyAlarm")
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle("Hi Reminder")
                .setContentText("ALARM! ALARM! ALARM!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);


        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);

        notificationManagerCompat.notify(200,builder.build());
    }
}
