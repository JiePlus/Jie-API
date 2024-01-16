package com.jie1234.project.common;

public enum SseAlgorithm {
    AES256("AES256"),
    AWS_KMS("aws:kms");

    private String algorithm;

    SseAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public String getAlgorithm() {
        return algorithm;
    }
}