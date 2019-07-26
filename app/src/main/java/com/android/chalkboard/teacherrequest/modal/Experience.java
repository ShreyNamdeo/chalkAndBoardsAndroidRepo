
package com.android.chalkboard.teacherrequest.modal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Experience {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("teacherId")
    @Expose
    private Integer teacherId;
    @SerializedName("institute")
    @Expose
    private String institute;
    @SerializedName("startDate")
    @Expose
    private Integer startDate;
    @SerializedName("designation")
    @Expose
    private String designation;
    @SerializedName("subject")
    @Expose
    private String subject;
    @SerializedName("endDate")
    @Expose
    private Integer endDate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Integer teacherId) {
        this.teacherId = teacherId;
    }

    public String getInstitute() {
        return institute;
    }

    public void setInstitute(String institute) {
        this.institute = institute;
    }

    public Integer getStartDate() {
        return startDate;
    }

    public void setStartDate(Integer startDate) {
        this.startDate = startDate;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Integer getEndDate() {
        return endDate;
    }

    public void setEndDate(Integer endDate) {
        this.endDate = endDate;
    }

}
