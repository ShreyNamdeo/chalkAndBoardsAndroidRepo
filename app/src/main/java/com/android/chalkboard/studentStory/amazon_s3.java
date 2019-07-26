package com.android.chalkboard.studentStory;

public class amazon_s3 {

    String s3Key,readUrl,writeUrl;

    public amazon_s3() {
    }

    public amazon_s3(String s3Key, String readUrl, String writeUrl) {
        this.s3Key = s3Key;
        this.readUrl = readUrl;
        this.writeUrl = writeUrl;
    }

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
