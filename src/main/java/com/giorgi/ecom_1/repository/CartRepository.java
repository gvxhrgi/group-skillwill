package com.giorgi.ecom_1.repository;

import com.giorgi.ecom_1.model.Cart;
import com.giorgi.ecom_1.model.Product;
import com.giorgi.ecom_1.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class CartRepository implements CartInterface<Cart> {
    private final Map<String, Cart> repository = new ConcurrentHashMap<>();
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Autowired
    public CartRepository(ProductRepository productRepository, UserRepository userRepository) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    private void validateCart(Cart cart, String productName) {
        if (cart == null) throw new IllegalArgumentException("Cart cannot be null");
        if (cart.getUserName() == null || cart.getUserName().isBlank())
            throw new IllegalArgumentException("User name cannot be blank");
        if (productRepository.retrieve(productName) == null)
            throw new IllegalArgumentException("Product not found");
    }

    private void validatePurchase(Cart cart) {
        if (cart.getProducts().isEmpty()) {
            throw new IndexOutOfBoundsException("Cannot purchase empty cart");
        }
    }

    private void validateAdmin(String userName) {
        User user = userRepository.retrieve(userName);
        if (user != null && user.getAdmin()) {
            throw new IllegalArgumentException("Admins cannot purchase products");
        }
    }

    @Override
    public void storeWithUser(String userName, String productName) {
        validateAdmin(userName);
        Cart cart = repository.computeIfAbsent(userName, Cart::new);
        validateCart(cart, productName);

        Product product = productRepository.retrieve(productName);
        if (product == null) {
            throw new IllegalArgumentException("Product not found");
        }

        cart.addProduct(product);
    }

    @Override
    public Cart retrieve(String userName) {
        if (userName == null || userName.isBlank()) {
            throw new IllegalArgumentException("User name cannot be blank");
        }
        return repository.computeIfAbsent(userName, Cart::new);
    }

    @Override
    public Collection<Cart> returnAll() {
        return repository.values();
    }

    @Override
    public Product updateWithUsername(String userName, Cart cart, String productName) {
        if (userName == null || userName.isBlank()) {
            throw new IllegalArgumentException("User name cannot be blank");
        }
        validateCart(cart, productName);
        repository.put(userName, cart);
        return null;
    }

    @Override
    public Cart delete(String userName) {
        if (userName == null || userName.isBlank()) {
            throw new IllegalArgumentException("User name cannot be blank");
        }
        return repository.remove(userName);
    }

    public void validateCartForPurchase(String userName) {
        Cart cart = retrieve(userName);
        validatePurchase(cart);
    }
}