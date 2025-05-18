package com.giorgi.ecom_1.repository;

import com.giorgi.ecom_1.model.Product;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class ProductRepository implements ProductInterface<Product> {
    private Map<String, Product> repository;

    public ProductRepository() {
        this.repository = new HashMap<>();
    }

    private void validateProduct(Product product) {
        if (product == null) throw new IllegalArgumentException("Product cannot be null");
        if (product.getProductName() == null || product.getProductName().isBlank())
            throw new IllegalArgumentException("Product name cannot be blank");
        if (product.getProductPrice() < 1)
            throw new IllegalArgumentException("Product price cannot be negative or 0");
        if (product.getInStock() < 1)
            throw new IllegalArgumentException("Stock quantity cannot be negative or 0");
        Product existingProduct = repository.get(product.getProductName());
        if (existingProduct != null) {
            throw new IllegalArgumentException("Product with this name already exists");
        }
    }

    @Override
    public void store(Product product) {
        for (Product k : repository.values()){
            if (Objects.equals(k.getProductName(), product.getProductName())){
                System.out.println("Product With This Name Already Exists");
                return;
            }
        }

        if (Objects.equals(product.getProductName(), "")) {
            System.out.println("Product Name Is Empty!");
        } else {
            repository.put(product.getProductName(), product);
        }
    }

    @Override
    public Product retrieve(String name) {
        if (name == null || name.trim().isEmpty()) {
            return null;
        }
        return repository.get(name);
    }

    @Override
    public Collection<Product> returnAll() {
        return repository.values();
    }

    @Override
    public Product delete(String name) {
        return repository.remove(name);
    }

    @Override
    public Product update(String productName, Product newProduct) {
        if (productName == null || productName.isBlank()) {
            throw new IllegalArgumentException("Product name cannot be null or blank");
        }
        validateProduct(newProduct);

        Product existingProduct = repository.get(productName);
        if (existingProduct == null) {
            throw new IllegalArgumentException("Product with name '" + productName + "' not found");
        }

        if (!productName.equalsIgnoreCase(newProduct.getProductName())) {
            repository.remove(productName);
            repository.put(newProduct.getProductName(), existingProduct);
        }

        existingProduct.setProductName(newProduct.getProductName());
        existingProduct.setProductPrice(newProduct.getProductPrice());
        existingProduct.setInStock(newProduct.getInStock());

        return existingProduct;
    }
}
