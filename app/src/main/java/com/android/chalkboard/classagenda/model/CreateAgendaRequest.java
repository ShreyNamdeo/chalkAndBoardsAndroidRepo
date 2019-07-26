package com.android.chalkboard.classagenda.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CreateAgendaRequest implements Serializable {

    @SerializedName("topic")
    @Expose
    private String topic;

    @SerializedName("agenda")
    @Expose
    private String agenda;

    public CreateAgendaRequest(String topic, String agenda) {
        this.topic = topic;
        this.agenda = agenda;
    }

    public CreateAgendaRequest(){}

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getAgenda() {
        return agenda;
    }

    public void setAgenda(String agenda) {
        this.agenda = agenda;
    }
}
