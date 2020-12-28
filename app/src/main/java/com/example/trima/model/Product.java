package com.example.trima.model;

public class Product {
    private String Pid;
    private String Date;
    private String Time;
    private String Description;
    private String Image;
    private String Category;
    private String Price;

    public String getPid() {
        return Pid;
    }

    public void setPid(String pid) {
        Pid = pid;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getProductDiscount() {
        return ProductDiscount;
    }

    public void setProductDiscount(String productDiscount) {
        ProductDiscount = productDiscount;
    }

    public int getProductQuantity() {
        return ProductQuantity;
    }

    public void setProductQuantity(int productQuantity) {
        ProductQuantity = productQuantity;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getSellerId() {
        return SellerId;
    }

    public void setSellerId(String sellerId) {
        SellerId = sellerId;
    }

    private String ProductDiscount;
    private int ProductQuantity;
    private String ProductName;
    private String SellerId;
    private Product(){

    }
    public Product(String pid, String date, String time, String description, String image, String category, String price, String productDiscount, int productQuantity, String productName, String sellerId){
        Pid = pid;
        Date = date;
        Time = time;
        Description = description;
        Image = image;

        Category = category;
        Price = price;
        ProductDiscount = productDiscount;
        ProductQuantity = productQuantity;
        ProductName = productName;
        SellerId = sellerId;
    }
}
