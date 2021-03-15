package com.example.reminder;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {

    final int add_reminder = 3;
    public static ArrayList<Reminder> reminders;
    public static String content;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        reminders = new ArrayList<>();

        createNotificationChannel();

        Button btnAdd = findViewById(R.id.btnAdd),
                btnView = findViewById(R.id.btnView);

        btnAdd.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this,
                    com.example.reminder.AddReminder.class);
            startActivityForResult(intent,add_reminder);
        });

        btnView.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this,
                    com.example.reminder.ViewReminder.class);
            startActivity(intent);
        });
    }

    private void createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            CharSequence name = "Reminder Channel";
            String description = "Alarm channel";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("Reminder",name,importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == add_reminder)
        {
            if(resultCode == RESULT_OK) {
                createAlarm(data.getStringExtra("schedule"));
                content = data.getStringExtra("description");
            }
            if (resultCode == RESULT_CANCELED)
                Toast.makeText(this, "Canceled", Toast.LENGTH_SHORT).show();
        }
    }

    private void createAlarm(String date)
    {
        Intent intent = new Intent(MainActivity.this, ReminderBroadCast.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this,0,intent,0);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        Date remind = new Date(date);

        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8:00"));
        calendar.setTime(remind);
        calendar.set(Calendar.SECOND,0);

        alarmManager.set(AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(), pendingIntent);
    }
}