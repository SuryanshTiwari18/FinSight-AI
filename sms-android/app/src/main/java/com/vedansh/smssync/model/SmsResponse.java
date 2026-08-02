package com.vedansh.smssync.model;

public class SmsResponse {

    private boolean success;
    private String message;

    public SmsResponse() {
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}