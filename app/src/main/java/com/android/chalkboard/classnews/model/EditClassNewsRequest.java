package com.android.chalkboard.classnews.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class EditClassNewsRequest implements Serializable {

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("content")
    @Expose
    private String content;

    @SerializedName("date")
    @Expose
    private String date;

    @SerializedName("topic")
    @Expose
    private String topic;

    @SerializedName("level")
    @Expose
    private String level = "CLASS";

    @SerializedName("numberOfImages")
    @Expose
    private int numberOfImages = 1;

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getDate() {
        return date;
    }

    public String getTopic() {
        return topic;
    }

    public String getLevel() {
        return level;
    }

    public int getNumberOfImages() {
        return numberOfImages;
    }

    public EditClassNewsRequest(int id, String title, String content, String date, String topic) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.date = date;
        this.topic = topic;
    }
}
