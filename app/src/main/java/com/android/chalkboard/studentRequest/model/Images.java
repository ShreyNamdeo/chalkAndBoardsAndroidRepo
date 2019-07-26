package com.android.chalkboard.studentRequest.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Images implements Serializable {
    @SerializedName("s3_key")
    @Expose
    private String s3_key;

    @SerializedName("readUrl")
    @Expose
    private String readUrl;

    @SerializedName("writeUrl")
    @Expose
    private String writeUrl;

    public String getS3_key() {
        return s3_key;
    }

    public void setS3_key(String s3_key) {
        this.s3_key = s3_key;
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
