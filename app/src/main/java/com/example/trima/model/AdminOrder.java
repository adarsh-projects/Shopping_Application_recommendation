package com.example.trima.model;

public class AdminOrder {
    private String TotalPrice;
    private String Uname;
    private String Uphone;

    public AdminOrder(String totalPrice, String uname, String uphone, String uaddress, String ucity, String data, String time) {
        TotalPrice = totalPrice;
        Uname = uname;
        Uphone = uphone;
        Uaddress = uaddress;
        Ucity = ucity;
        Data = data;
        Time = time;
    }

    public String getTotalPrice() {
        return TotalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        TotalPrice = totalPrice;
    }

    public String getUname() {
        return Uname;
    }

    public void setUname(String uname) {
        Uname = uname;
    }

    public String getUphone() {
        return Uphone;
    }

    public void setUphone(String uphone) {
        Uphone = uphone;
    }

    public String getUaddress() {
        return Uaddress;
    }

    public void setUaddress(String uaddress) {
        Uaddress = uaddress;
    }

    public String getUcity() {
        return Ucity;
    }

    public void setUcity(String ucity) {
        Ucity = ucity;
    }

    public String getData() {
        return Data;
    }

    public void setData(String data) {
        Data = data;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    private String Uaddress;
    private String Ucity;
    private String Data;
    private String Time;

    public AdminOrder(){

    }



}
