package com.example.reminder;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;



public class ViewReminder extends AppCompatActivity {



    ReminderAdapter reminderAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_reminder);

        ListView listView = findViewById(R.id.lvList);
        reminderAdapter= new ReminderAdapter(ViewReminder.this, MainActivity.reminders);
        listView.setAdapter(reminderAdapter);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1)
        {
            reminderAdapter.notifyDataSetChanged();
        }

    }

}