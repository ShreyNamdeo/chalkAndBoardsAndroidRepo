package com.android.chalkboard.studentStory.reports.Models;

import java.util.ArrayList;

public class resultModel {

    private String subject;
    private ArrayList<examModel> marks;

    public resultModel() {
    }

    public resultModel(String subject, ArrayList<examModel> marks) {
        this.subject = subject;
        this.marks = marks;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public ArrayList<examModel> getMarks() {
        return marks;
    }

    public void setMarks(ArrayList<examModel> marks) {
        this.marks = marks;
    }
}
