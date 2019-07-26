package com.android.chalkboard.studentStory.attendance;

public class timelineModel {

    String date;
    boolean present;

    public timelineModel(String date, boolean attendance) {
        this.date = date;
        this.present = attendance;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean getAttendance() {
        return present;
    }

    public void setAttendance(boolean attendance) {
        this.present = attendance;
    }
}
