package com.giorgi.ecom_1.model;

import org.springframework.boot.context.properties.bind.DefaultValue;

public class Product {
    private String productName;
    private String addedBy;
    private int productPrice;
    private int inStock;

    public Product(String productName, int productPrice, int inStock) {
        this.productName = productName;
        this.productPrice = productPrice;
        this.inStock = inStock;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(int productPrice) {
        this.productPrice = productPrice;
    }

    public int getInStock() {
        return inStock;
    }

    public void setInStock(int inStock) {
        this.inStock = inStock;
    }

    public String getAddedBy() {
        return addedBy;
    }

    public void setAddedBy(String addedBy) {
        this.addedBy = addedBy;
    }
}