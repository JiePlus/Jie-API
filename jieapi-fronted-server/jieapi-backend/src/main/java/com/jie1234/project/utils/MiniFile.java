package com.jie1234.project.utils;

public class MiniFile {
    private String bucketName;
    private String fileName;

    public MiniFile(String bucketName, String fileName) {
        this.bucketName = bucketName;
        this.fileName = fileName;
    }

    public MiniFile() {
    }

    public String getBucketName() {
        return bucketName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}