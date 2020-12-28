package com.example.trima.model;

public class User {
    private String image;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    private String address;
    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    private String UserName;

    public User(String image, String address, String userName, String phone, String password) {
        this.image = image;
        this.address = address;
        UserName = userName;
        Phone = phone;
        Password = password;
    }

    private String Phone;
    private String Password;
    public User(){

    }
}
