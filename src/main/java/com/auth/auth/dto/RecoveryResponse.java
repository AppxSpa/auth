package com.auth.auth.dto;

public class RecoveryResponse {

    private String message;
    private String email;

    public RecoveryResponse(String message, String email) {
        this.message = message;
        this.email = email;
    }

    public String getMessage() {
        return message;
    }

    public String getEmail() {
        return email;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}