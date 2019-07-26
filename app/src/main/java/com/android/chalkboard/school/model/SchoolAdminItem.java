package com.android.chalkboard.school.model;

public class SchoolAdminItem {
    private String schoolName;
    private String schoolAddress;
    private int schoolImage;

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getSchoolAddress() {
        return schoolAddress;
    }

    public void setSchoolAddress(String schoolAddress) {
        this.schoolAddress = schoolAddress;
    }

    public int getSchoolImage() {
        return schoolImage;
    }

    public void setSchoolImage(int schoolImage) {
        this.schoolImage = schoolImage;
    }

    public SchoolAdminItem(String schoolName, String schoolAddress, int schoolImage) {
        this.schoolName = schoolName;
        this.schoolAddress = schoolAddress;
        this.schoolImage = schoolImage;

    }
}
