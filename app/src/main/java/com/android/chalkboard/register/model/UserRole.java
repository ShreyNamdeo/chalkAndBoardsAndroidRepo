package com.android.chalkboard.register.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UserRole implements Serializable
{

    @SerializedName("id")
    @Expose
    private String id;
    private final static long serialVersionUID = -7467051697835593306L;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
