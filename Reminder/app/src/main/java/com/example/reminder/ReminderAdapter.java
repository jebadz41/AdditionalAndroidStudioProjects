package com.example.reminder;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.w3c.dom.Text;

import java.util.List;


class ReminderAdapter extends ArrayAdapter<Reminder> {

    private Context context;
    private List<Reminder> reminders;

    public ReminderAdapter(Context context, List<Reminder> reminders)
    {
        super(context,R.layout.reminder_list, reminders);

        this.context = context;
        this.reminders = reminders;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        convertView = inflater.inflate(R.layout.reminder_list, parent, false);

        TextView tvDescription = convertView.findViewById(R.id.tvDescription),
                tvSchedule = convertView.findViewById(R.id.tvSched);

        tvDescription.setText(reminders.get(position).getDescription());
        tvSchedule.setText(reminders.get(position).getSchedule().toString());

        return convertView;
    }
}