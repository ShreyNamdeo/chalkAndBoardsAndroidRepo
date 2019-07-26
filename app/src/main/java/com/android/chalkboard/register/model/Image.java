package com.android.chalkboard.register.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Image implements Serializable {

    @SerializedName("s3_key")
    @Expose
    private String s3Key;
    @SerializedName("readUrl")
    @Expose
    private String readUrl;
    @SerializedName("writeUrl")
    @Expose
    private String writeUrl;
    private final static long serialVersionUID = -3463455196575362126L;

    public String getS3Key() {
        return s3Key;
    }

    public void setS3Key(String s3Key) {
        this.s3Key = s3Key;
    }

    public String getReadUrl() {
        return readUrl;
    }

    public void setReadUrl(String readUrl) {
        this.readUrl = readUrl;
    }

    public String getWriteUrl() {
        return writeUrl;
    }

    public void setWriteUrl(String writeUrl) {
        this.writeUrl = writeUrl;
    }

}
