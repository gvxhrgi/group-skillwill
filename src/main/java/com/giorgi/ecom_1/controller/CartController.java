package com.giorgi.ecom_1.controller;

import com.giorgi.ecom_1.model.Cart;
import com.giorgi.ecom_1.model.Product;
import com.giorgi.ecom_1.model.User;
import com.giorgi.ecom_1.repository.CartRepository;
import com.giorgi.ecom_1.repository.ProductRepository;
import com.giorgi.ecom_1.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@RestController
public class CartController {
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final CartRepository cartRepository;

    public CartController(ProductRepository productRepository, UserRepository userRepository, CartRepository cartRepository) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
    }

    @GetMapping(value = "/cart")
    @Operation(summary = "Show Cart Of Users")
    public ResponseEntity<Collection<Cart>> getItems() {
        return ResponseEntity.ok(cartRepository.returnAll());
    }

    @GetMapping(value = "/cart/{userName}")
    @Operation(summary = "Show Cart Of Specific User")
    public ResponseEntity<?> getCart(@PathVariable String userName) {
        try {
            Cart cart = cartRepository.retrieve(userName);
            return ResponseEntity.ok(cart);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error retrieving cart: " + e.getMessage());
        }
    }

    @PostMapping(value = "/add-item")
    @Operation(summary = "Add an item to cart")
    public ResponseEntity<?> addItem(@RequestParam String userName, @RequestParam String productName) {
        try {
            User user = userRepository.retrieve(userName);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("User not found: " + userName);
            }

            Product product = productRepository.retrieve(productName);
            if (product == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Product not found: " + productName);
            }

            if (product.getInStock() <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Cannot add product to cart. Product '" + productName + "' is out of stock.");
            }

            cartRepository.storeWithUser(userName, productName);
            return ResponseEntity.ok("Item added to cart successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error adding item to cart: " + e.getMessage());
        }
    }

    @PostMapping(value = "/buy-items")
    @Operation(summary = "Buy items from cart")
    public ResponseEntity<?> buyItems(@RequestParam String userName) {
        try {
            User user = userRepository.retrieve(userName);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("User not found: " + userName);
            }

            Cart cart = cartRepository.retrieve(userName);
            if (cart == null || cart.getProducts().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Cart is empty for user: " + userName);
            }

            double currentBalance = user.getBalance();
            double totalPrice = cart.getTotalPrice();

            if (currentBalance < totalPrice) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Insufficient balance. Required: " + totalPrice + ", Available: " + currentBalance);
            }

            for (Product product : cart.getProducts()) {
                if (product.getInStock() <= 0) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body("Product '" + product.getProductName() + "' is out of stock");
                }
            }

            user.setBalance(currentBalance - totalPrice);

            for (Product product : cart.getProducts()) {
                product.setInStock(product.getInStock() - 1);
            }

            cart.clearCart();

            return ResponseEntity.ok("Purchase completed successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error processing purchase: " + e.getMessage());
        }
    }

    @PostMapping(value = "/buy-item/{productName}")
    @Operation(summary = "Buy Specific Item")
    public ResponseEntity<?> buyItem(@RequestParam String userName, @PathVariable String productName) {
        try {
            User user = userRepository.retrieve(userName);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("User not found: " + userName);
            }

            Product product = productRepository.retrieve(productName);
            if (product == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Product not found: " + productName);
            }

            if (product.getInStock() <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Product '" + productName + "' is out of stock");
            }

            double currentBalance = user.getBalance();
            double productPrice = product.getProductPrice();

            if (currentBalance < productPrice) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Insufficient balance. Required: " + productPrice + ", Available: " + currentBalance);
            }

            user.setBalance(currentBalance - productPrice);
            product.setInStock(product.getInStock() - 1);

            return ResponseEntity.ok("Item purchased successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error purchasing item: " + e.getMessage());
        }
    }

    @DeleteMapping(value = "/remove-item/{productName}")
    @Operation(summary = "Remove Item From Cart")
    public ResponseEntity<?> removeItem(@PathVariable String productName, @RequestParam String userName) {
        try {
            Cart cart = cartRepository.retrieve(userName);
            if (cart == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Cart not found for user: " + userName);
            }

            boolean removed = cart.removeProduct(productName);
            if (!removed) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Product not found in cart: " + productName);
            }

            return ResponseEntity.ok("Item removed from cart successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error removing item from cart: " + e.getMessage());
        }
    }

    @DeleteMapping(value = "/cart/{userName}/clear")
    @Operation(summary = "Clear Cart")
    public ResponseEntity<?> clearCart(@PathVariable String userName) {
        try {
            Cart cart = cartRepository.retrieve(userName);
            if (cart == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Cart not found for user: " + userName);
            }

            cart.clearCart();
            return ResponseEntity.ok("Cart cleared successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error clearing cart: " + e.getMessage());
        }
    }
}