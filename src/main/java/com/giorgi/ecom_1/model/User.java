package com.giorgi.ecom_1.model;

import java.util.Collection;
import java.util.List;

public class User {
    private String userName;
    private String userMail;
    private String userPassword;
    private Boolean isAdmin;
    private double balance;

    public User(String userName, String userMail, String userPassword, Integer balance) {
        this.userName = userName;
        this.userMail = userMail;
        this.userPassword = userPassword;
        this.balance = balance;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserMail() {
        return userMail;
    }

    public void setUserMail(String userMail) {
        this.userMail = userMail;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public Boolean getAdmin() {
        return isAdmin;
    }

    public void setAdmin(Boolean admin) {
        isAdmin = admin;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
