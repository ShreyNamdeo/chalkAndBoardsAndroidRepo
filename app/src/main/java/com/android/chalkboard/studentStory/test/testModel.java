package com.android.chalkboard.studentStory.test;

import com.android.chalkboard.studentStory.amazon_s3;

import java.util.ArrayList;

public class testModel {

    int classId,totalMarks,obtainedMarks,attemptsAllowed,numberOfImage;
    String title,date,level,content;
    ArrayList<amazon_s3> images;

    public testModel(int classId, int totalMarks, int obtainedMarks, int attemptsAllowed, int numberOfImage, String title, String date, String level, String content, ArrayList<amazon_s3> images) {
        this.classId = classId;
        this.totalMarks = totalMarks;
        this.obtainedMarks = obtainedMarks;
        this.attemptsAllowed = attemptsAllowed;
        this.numberOfImage = numberOfImage;
        this.title = title;
        this.date = date;
        this.level = level;
        this.content = content;
        this.images = images;
    }

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public int getTotalMarks() {
        return totalMarks;
    }

    public void setTotalMarks(int totalMarks) {
        this.totalMarks = totalMarks;
    }

    public int getObtainedMarks() {
        return obtainedMarks;
    }

    public void setObtainedMarks(int obtainedMarks) {
        this.obtainedMarks = obtainedMarks;
    }

    public int getAttemptsAllowed() {
        return attemptsAllowed;
    }

    public void setAttemptsAllowed(int attemptsAllowed) {
        this.attemptsAllowed = attemptsAllowed;
    }

    public int getNumberOfImage() {
        return numberOfImage;
    }

    public void setNumberOfImage(int numberOfImage) {
        this.numberOfImage = numberOfImage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ArrayList<amazon_s3> getImages() {
        return images;
    }

    public void setImages(ArrayList<amazon_s3> images) {
        this.images = images;
    }
}
