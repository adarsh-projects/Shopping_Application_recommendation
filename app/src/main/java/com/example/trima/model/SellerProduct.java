package com.example.trima.model;

public class SellerProduct {

    private String Category;
    private String Date;
    private String Description;
    private String Image;
    private String Pid;
    private String Price;

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
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

    public String getPid() {
        return Pid;
    }

    public void setPid(String pid) {
        Pid = pid;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getProductState() {
        return ProductState;
    }

    public void setProductState(String productState) {
        ProductState = productState;
    }

    public String getSellerId() {
        return SellerId;
    }

    public void setSellerId(String sellerId) {
        SellerId = sellerId;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    private String ProductName;
    private String ProductState;
    private String SellerId;
    private String Time;

    public SellerProduct(){

    }
    public SellerProduct(String category, String date, String description, String image, String pid, String price, String productName, String productState, String sellerId, String time) {
        Category = category;
        Date = date;
        Description = description;
        Image = image;
        Pid = pid;
        Price = price;
        ProductName = productName;
        ProductState = productState;
        SellerId = sellerId;
        Time = time;
    }
}
