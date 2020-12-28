package com.example.trima.model;

public class Cart {
    private String Pid, Pname, Price;
    private int quantity;
    public Cart(String pid, String pname, String price, int quantity){
        Pid = pid;
        Pname = pname;
        Price = price;
        this.quantity = quantity;
    }
    public Cart(){

    }
    public String getPname() {
        return Pname;
    }

    public void setPname(String pname) {
        Pname = pname;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getPid() {
        return Pid;
    }

    public void setPid(String pid) {
        Pid = pid;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
