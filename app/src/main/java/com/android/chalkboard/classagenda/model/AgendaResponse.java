package com.android.chalkboard.classagenda.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class AgendaResponse implements Serializable {

    @SerializedName("agenda")
    @Expose
    private String agenda;

    @SerializedName("classId")
    @Expose
    private int classId;

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("modifiedDate")
    @Expose
    private String modifiedDate;

    @SerializedName("teacherId")
    @Expose
    private int teacherId;

    @SerializedName("topic")
    @Expose
    private String topic;

    public AgendaResponse(){}

    public String getAgenda() {
        return agenda;
    }

    public void setAgenda(String agenda) {
        this.agenda = agenda;
    }

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(String modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public int getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }
}
