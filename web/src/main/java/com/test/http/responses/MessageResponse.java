package com.test.http.responses;

public class MessageResponse {
    private boolean status;

    private String message;

    public MessageResponse(boolean status) {
        this.status = status;
    }

    public MessageResponse(boolean status, String message) {
        this.status = status;
        this.message = message;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}