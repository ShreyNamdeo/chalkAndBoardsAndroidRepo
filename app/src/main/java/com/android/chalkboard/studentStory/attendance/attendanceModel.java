package com.android.chalkboard.studentStory.attendance;

import java.util.ArrayList;

public class attendanceModel {

    private String date;
    private ArrayList<recordsModel> recordsModels;

    public attendanceModel() {
    }

    public attendanceModel(String date, ArrayList<recordsModel> recordsModels) {
        this.date = date;
        this.recordsModels = recordsModels;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ArrayList<recordsModel> getRecordsModels() {
        return recordsModels;
    }

    public void setRecordsModels(ArrayList<recordsModel> recordsModels) {
        this.recordsModels = recordsModels;
    }
}
