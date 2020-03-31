package com.example.pezmercado.Model;

public class Cart {
    private String pid, scientificName, date, price, quantity, discount, time;

    public Cart(){

    }

    public Cart(String pid, String scientificName, String date, String price, String quantity, String discount, String time) {
        this.pid = pid;
        this.scientificName = scientificName;
        this.date = date;
        this.price = price;
        this.quantity = quantity;
        this.discount = discount;
        this.time = time;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getScientificName() {
        return scientificName;
    }

    public void setScientificName(String scientificName) {
        this.scientificName = scientificName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
