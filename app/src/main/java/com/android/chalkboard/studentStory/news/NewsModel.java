package com.android.chalkboard.studentStory.news;

import com.android.chalkboard.studentStory.amazon_s3;

public class NewsModel {

    int id;
    String title,content,topic,level;
    String date;
    amazon_s3 image;

    public NewsModel() {
    }

    public NewsModel(int id, String title, String content, String topic, String level, String date, amazon_s3 image) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.topic = topic;
        this.level = level;
        this.date = date;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public amazon_s3 getImage() {
        return image;
    }

    public void setImage(amazon_s3 image) {
        this.image = image;
    }
}
