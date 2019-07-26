package com.android.chalkboard.studentStory.topics;

import java.util.ArrayList;

public class topicsModel {

    String subject;
    ArrayList<String> topics;

    public topicsModel(String subject, ArrayList<String> topics) {
        this.subject = subject;
        this.topics = topics;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public ArrayList<String> getTopics() {
        return topics;
    }

    public void setTopics(ArrayList<String> topics) {
        this.topics = topics;
    }
}
