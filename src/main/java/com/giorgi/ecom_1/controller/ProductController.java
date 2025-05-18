package com.giorgi.ecom_1.controller;

import com.giorgi.ecom_1.model.Product;
import com.giorgi.ecom_1.model.User;
import com.giorgi.ecom_1.repository.ProductRepository;
import com.giorgi.ecom_1.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
public class ProductController {
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public ProductController(ProductRepository productRepository, UserRepository userRepository) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    @GetMapping(value = "/products")
    @Operation(summary = "Return All Products")
    public Collection<Product> getProducts() {
        return productRepository.returnAll();
    }

    @PostMapping("/add-product")
    @Operation(summary = "Add a product")
    public ResponseEntity<?> addProduct(@RequestBody Product product, @RequestParam String userName) {
        try {
            if (userName == null || userName.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Username cannot be empty");
            }

            User user = userRepository.retrieve(userName);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("User does not exist in the system");
            }

            if (user.getAdmin() == null || !user.getAdmin()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Only administrators can add products");
            }

            product.setAddedBy(userName);
            productRepository.store(product);

            Product storedProduct = productRepository.retrieve(product.getProductName());
            if (storedProduct == null) {
                return ResponseEntity.internalServerError()
                        .body("Failed to store product in repository");
            }

            return ResponseEntity.ok(storedProduct);

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Error processing request: " + e.getMessage());
        }
    }

    @DeleteMapping(value = "/remove-product")
    @Operation(summary = "Delete a product")
    public void deleteitem(@RequestParam String name){
        productRepository.delete(name);
    }

    @PutMapping("/update/{productName}")
    @Operation(summary = "Update a product")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully updated product"),
            @ApiResponse(responseCode = "400", description = "Product with such name was not found"),
            @ApiResponse(responseCode = "500", description = "Internal error")
    })
    public Product updateProductByName(@PathVariable("productName") String productName, @RequestBody Product product){
        return productRepository.update(productName, product);
    }
}