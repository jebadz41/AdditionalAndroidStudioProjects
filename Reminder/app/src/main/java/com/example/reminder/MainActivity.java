package com.example.reminder;

import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    public static ArrayList<Reminder> reminders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        reminders = new ArrayList<>();



        Button btnAdd = findViewById(R.id.btnAdd),
                btnView = findViewById(R.id.btnView);

        btnAdd.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this,
                    com.example.reminder.AddReminder.class);
            startActivity(intent);
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

}