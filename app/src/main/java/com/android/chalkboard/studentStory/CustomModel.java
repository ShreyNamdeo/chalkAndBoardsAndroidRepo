package com.android.chalkboard.studentStory;

public class CustomModel {

    int id;
    String name;
    String desc,address,principal;
    int perc;

    public CustomModel(int id, String name, String desc,String address,String principal) {
        this.id = id;
        this.name = name;
        this.desc = desc;
        this.address = address;
        this.principal = principal;
    }

    public CustomModel(int id, String name, String desc) {
        this.id = id;
        this.name = name;
        this.desc = desc;
    }

    public CustomModel(int id, String name, int perc) {
        this.id = id;
        this.name = name;
        this.desc = desc;
        this.perc = perc;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPrincipal() {
        return principal;
    }

    public void setPrincipal(String principal) {
        this.principal = principal;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPerc() {
        return perc;
    }

    public void setPerc(int perc) {
        this.perc = perc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
