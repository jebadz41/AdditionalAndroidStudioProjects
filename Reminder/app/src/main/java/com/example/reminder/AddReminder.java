package com.example.reminder;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class AddReminder extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reminder);

        TextView tvSched = findViewById(R.id.tvSched);
        EditText etDescription = findViewById(R.id.etDescription);
        Button btnSelect = findViewById(R.id.btnSelect),
            btnAddReminder = findViewById(R.id.btnAddReminder),
            btnCancel = findViewById(R.id.btnCancel);

        final Calendar newCalender = Calendar.getInstance();

        btnSelect.setOnClickListener(v -> {
            final Calendar newDate = Calendar.getInstance();
            DatePickerDialog datePickerDialog = new DatePickerDialog(AddReminder.this, (view, year, month, dayOfMonth) -> {
                    Calendar newTime = Calendar.getInstance();
                    TimePickerDialog time = new TimePickerDialog(AddReminder.this, (view1, hourOfDay, minute) -> {
                        newDate.set(year,month,dayOfMonth,hourOfDay,minute,0);
                        Calendar tem = Calendar.getInstance();
                        if(newDate.getTimeInMillis() - tem.getTimeInMillis()>0)
                            tvSched.setText(newDate.getTime().toString());
                        else
                            Toast.makeText(AddReminder.this, "Invalid Tme", Toast.LENGTH_SHORT).show();
                    },newTime.get(Calendar.HOUR_OF_DAY),newTime.get(Calendar.MINUTE),true);
                time.show();
            },newCalender.get(Calendar.YEAR),newCalender.get(Calendar.MONTH),newCalender.get(Calendar.DAY_OF_MONTH));

            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
            datePickerDialog.show();
        });

        btnAddReminder.setOnClickListener(v -> {

            if(etDescription.getText().toString().isEmpty() || (tvSched.getText().toString().equals(R.string.date_time)))
                Toast.makeText(AddReminder.this, "Enter all fields", Toast.LENGTH_SHORT).show();
            else {
                String description = etDescription.getText().toString().trim();
                Date schedule = new Date(tvSched.getText().toString().trim());

                MainActivity.reminders.add(new Reminder(description,schedule));
                Intent intent = new Intent();
                intent.putExtra("description",description);
                intent.putExtra("schedule",schedule.toString());
                setResult(RESULT_OK,intent);

                Toast.makeText(AddReminder.this, "Reminder Successfully added!", Toast.LENGTH_SHORT).show();

                AddReminder.this.finish();
            }
        });

        btnCancel.setOnClickListener( v -> {
            setResult(RESULT_CANCELED);
            AddReminder.this.finish();
        });
    }

}