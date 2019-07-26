package com.android.chalkboard.teacherrequest.modal;

public class TeachersRequestDetails {

    public TeachersRequestDetails(String teachersName, String subjectName, int teachersImage) {
        this.teachersName = teachersName;
        this.subjectName = subjectName;
        this.teachersImage = teachersImage;
    }

    private String teachersName;

    public int getTeachersImage() {
        return teachersImage;
    }

    public void setTeachersImage(int teachersImage) {
        this.teachersImage = teachersImage;
    }

    private int teachersImage;

    public String getTeachersName() {
        return teachersName;
    }

    public void setTeachersName(String teachersName) {
        this.teachersName = teachersName;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    private String subjectName;
}
