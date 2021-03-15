package com.example.reminder;

import java.util.Date;

public class Reminder {

    private String description;
    private Date schedule;

    public Reminder(String description, Date schedule) {
        this.description = description;
        this.schedule = schedule;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getSchedule() {
        return schedule;
    }

    public void setSchedule(Date schedule) {
        this.schedule = schedule;
    }
}
