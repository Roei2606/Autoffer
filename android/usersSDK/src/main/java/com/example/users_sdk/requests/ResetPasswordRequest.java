package com.example.users_sdk.requests;

public class ResetPasswordRequest {
    private String phoneNumber;
    private String newPassword;

    public ResetPasswordRequest() {
        // Required for serialization/deserialization
    }

    public ResetPasswordRequest(String phoneNumber, String newPassword) {
        this.phoneNumber = phoneNumber;
        this.newPassword = newPassword;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
