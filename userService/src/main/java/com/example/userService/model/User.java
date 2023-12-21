package com.example.userService.model;

public class User {

    private int userID;

    private String emailID;

    private String password;

    private String name;

    private String phoneNumber;

    private int isAdmin;

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getEmailID() {
        return emailID;
    }

    public void setEmailID(String emailID) {
        this.emailID = emailID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getAdmin() {
        return isAdmin;
    }

    public void setAdmin(int isAdmin) {
        this.isAdmin = isAdmin;
    }
}