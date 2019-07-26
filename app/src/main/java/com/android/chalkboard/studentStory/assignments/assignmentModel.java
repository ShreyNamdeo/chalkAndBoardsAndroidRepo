package com.android.chalkboard.studentStory.assignments;

import com.android.chalkboard.studentStory.amazon_s3;

import java.util.ArrayList;

public class assignmentModel {

    int id,classId,institutionId;
    String topic,content;
    String startDate,endDate;
    ArrayList<amazon_s3> images;

    public assignmentModel() {
    }

    public assignmentModel(int id, int classId, int institutionId, String topic, String content, String date, String endDate, ArrayList<amazon_s3> images) {
        this.id = id;
        this.classId = classId;
        this.institutionId = institutionId;
        this.topic = topic;
        this.content = content;
        this.startDate = date;
        this.endDate = endDate;
        this.images = images;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public int getInstitutionId() {
        return institutionId;
    }

    public void setInstitutionId(int institutionId) {
        this.institutionId = institutionId;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public ArrayList<amazon_s3> getImages() {
        return images;
    }

    public void setImages(ArrayList<amazon_s3> images) {
        this.images = images;
    }
}
