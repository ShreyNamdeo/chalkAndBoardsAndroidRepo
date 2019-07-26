package com.android.chalkboard.news.modal;

public class NewsRequest {
    /**
     * {
     * "title”: “Today’s news story",
     * "content”: “Data about the news story",
     * “date”: 15009300042, //In Milliseconds
     * “topic”: “About which topics”,
     * “level”: “INSTITUTION” //Always INSTITUTION for this api
     * “numberOfImages”: 1 //news can have only 1 image.
     * }
     */

    private String title;
    private String content;
    private String date;
    private String topic;
    private String level;
    private String numberOfImages;

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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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

    public String getNumberOfImages() {
        return numberOfImages;
    }

    public void setNumberOfImages(String numberOfImages) {
        this.numberOfImages = numberOfImages;
    }


}
