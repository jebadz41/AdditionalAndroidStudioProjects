package com.example.basicalarmreminder;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createNotificationChannel();

        Button btnClick = findViewById(R.id.btnClick);

        btnClick.setOnClickListener( v ->{
            Toast.makeText(this, "Reminder SET!", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(MainActivity.this, ReminderBroadCast.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this,0,intent,0);

            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

            long timeAtButtonClick  = System.currentTimeMillis();

            long ten = 1000 * 10;

            alarmManager.set(AlarmManager.RTC_WAKEUP,
                    timeAtButtonClick + ten, pendingIntent);

        });
    }
    private void createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            CharSequence name = "Reminder Channel";
            String description = "Alarm channel";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("notifyAlarm",name,importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}