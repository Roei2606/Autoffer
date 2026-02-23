package com.example.users_sdk.requests;


import com.example.core_models_sdk.models.UserType;

public class RegisterUserRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phoneNumber;
    private String address;
    private UserType userType;

    public RegisterUserRequest() {
    }

    public RegisterUserRequest(String firstName, String lastName, String email, String password,
                               String phoneNumber, String address, UserType userType) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.userType = userType;
    }

    public String getFirstName() {
        return firstName;
    }

    public RegisterUserRequest setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public RegisterUserRequest setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public RegisterUserRequest setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public RegisterUserRequest setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public RegisterUserRequest setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public String getAddress() {
        return address;
    }

    public RegisterUserRequest setAddress(String address) {
        this.address = address;
        return this;
    }

    public UserType getProfileType() {
        return userType;
    }

    public RegisterUserRequest setProfileType(UserType userType) {
        this.userType = userType;
        return this;
    }
}
