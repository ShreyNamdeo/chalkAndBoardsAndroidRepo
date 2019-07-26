package com.android.chalkboard.studentStory.attendance;

public class recordsModel {

    private int classId;
    private String name;
    private boolean present;

    public recordsModel() {
    }

    public recordsModel(int classId, String name, boolean present) {
        this.classId = classId;
        this.name = name;
        this.present = present;
    }

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getPresent() {
        return present;
    }

    public void setPresent(boolean present) {
        this.present = present;
    }

}
