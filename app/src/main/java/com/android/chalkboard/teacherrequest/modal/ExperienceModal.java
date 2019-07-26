package com.android.chalkboard.teacherrequest.modal;

public class ExperienceModal {

    public ExperienceModal(String startDate, String endDate, String institute, String subject, String designation) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.institute = institute;
        this.subject = subject;
        this.designation = designation;
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

    public String getInstitute() {
        return institute;
    }

    public void setInstitute(String institute) {
        this.institute = institute;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    private String startDate, endDate, institute, subject, designation;
}
