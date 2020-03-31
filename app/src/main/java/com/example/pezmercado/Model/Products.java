package com.example.pezmercado.Model;

public class Products {
    private String scientificName, commonName, localName, productForm, category;
    private String date, dateCaught, placeCaught, minOrder;
    private String pid, time, price, stock, image, availability;

    public Products()
    {

    }

    public Products(String scientificName, String commonName, String localName, String productForm, String category, String date, String dateCaught, String placeCaught, String minOrder, String pid, String time, String price, String stock, String image, String availability) {
        this.scientificName = scientificName;
        this.commonName = commonName;
        this.localName = localName;
        this.productForm = productForm;
        this.category = category;
        this.date = date;
        this.dateCaught = dateCaught;
        this.placeCaught = placeCaught;
        this.minOrder = minOrder;
        this.pid = pid;
        this.time = time;
        this.price = price;
        this.stock = stock;
        this.image = image;
        this.availability = availability;
    }

    public String getScientificName() {
        return scientificName;
    }

    public void setScientificName(String scientificName) {
        this.scientificName = scientificName;
    }

    public String getCommonName() {
        return commonName;
    }

    public void setCommonName(String commonName) {
        this.commonName = commonName;
    }

    public String getLocalName() {
        return localName;
    }

    public void setLocalName(String localName) {
        this.localName = localName;
    }

    public String getProductForm() {
        return productForm;
    }

    public void setProductForm(String productForm) {
        this.productForm = productForm;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDateCaught() {
        return dateCaught;
    }

    public void setDateCaught(String dateCaught) {
        this.dateCaught = dateCaught;
    }

    public String getPlaceCaught() {
        return placeCaught;
    }

    public void setPlaceCaught(String placeCaught) {
        this.placeCaught = placeCaught;
    }

    public String getMinOrder() {
        return minOrder;
    }

    public void setMinOrder(String minOrder) {
        this.minOrder = minOrder;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }
}
