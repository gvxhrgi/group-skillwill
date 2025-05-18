package com.giorgi.ecom_1.model;

import java.util.ArrayList;
import java.util.List;

public class Cart {
    private String userName;
    private List<Product> products;
    private double totalPrice;

    public Cart(String userName) {
        this.userName = userName;
        this.products = new ArrayList<>();
        this.totalPrice = 0.0;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
        calculateTotalPrice();
    }

    public double getTotalPrice() {
        return totalPrice;
    }
    public boolean addProduct(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }

        if (product.getInStock() <= 0) {
            return false;
        }

        products.add(product);
        calculateTotalPrice();
        return true;
    }

    /**
     * Removes a product from the cart
     *
     * @param productName the name of the product to remove
     * @return true if removed successfully, false if product not found in cart
     */
    public boolean removeProduct(String productName) {
        if (productName == null || productName.isBlank()) {
            throw new IllegalArgumentException("Product name cannot be blank");
        }

        boolean removed = products.removeIf(p -> p.getProductName().equals(productName));
        if (removed) {
            calculateTotalPrice();
        }
        return removed;
    }

    /**
     * Clears all products from the cart
     */
    public void clearCart() {
        products.clear();
        totalPrice = 0.0;
    }

    /**
     * Calculates the total price of all products in the cart
     */
    private void calculateTotalPrice() {
        totalPrice = products.stream()
                .mapToDouble(Product::getProductPrice)
                .sum();
    }

    /**
     * Returns the quantity of products in the cart
     */
    public int getItemCount() {
        return products.size();
    }

    @Override
    public String toString() {
        return "Cart{" +
                "userName='" + userName + '\'' +
                ", products=" + products +
                ", totalPrice=" + totalPrice +
                '}';
    }
}