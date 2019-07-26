package com.android.chalkboard.studentStory.reports.Models;

public class examModel {

    private int id,total,obtained,classId;
    double percentage;
    private String examName,level,result,date;

    public examModel() {
    }

    public examModel(int id, int total, int obtained, double percentage, String examName, String level, int classId, String result, String date) {
        this.id = id;
        this.total = total;
        this.obtained = obtained;
        this.percentage = percentage;
        this.examName = examName;
        this.level = level;
        this.classId = classId;
        this.result = result;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getObtained() {
        return obtained;
    }

    public void setObtained(int obtained) {
        this.obtained = obtained;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

    public String getExamName() {
        return examName;
    }

    public void setExamName(String examName) {
        this.examName = examName;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
