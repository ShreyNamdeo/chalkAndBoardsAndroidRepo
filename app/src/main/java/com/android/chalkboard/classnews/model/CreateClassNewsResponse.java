package com.android.chalkboard.classnews.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CreateClassNewsResponse implements Serializable {

    @SerializedName("content")
    @Expose
    private String content;

    @SerializedName("date")
    @Expose
    private String date;

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("image")
    @Expose
    private Images image;

    @SerializedName("level")
    @Expose
    private String level;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("topic")
    @Expose
    private String topic;

    private int classId;

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Images getImage() {
        return image;
    }

    public void setImage(Images image) {
        this.image = image;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }
}
