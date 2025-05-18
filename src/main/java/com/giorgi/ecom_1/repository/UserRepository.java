package com.giorgi.ecom_1.repository;

import com.giorgi.ecom_1.model.Product;
import com.giorgi.ecom_1.model.User;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class UserRepository implements UserInterface<User> {
    private Map<String, User> repository;

    public UserRepository() {
        this.repository = new HashMap<>();
    }

    private void validateUser(User user) {
        User existingUser = repository.get(user.getUserName());
        if (user == null)
            throw new IllegalArgumentException("User cannot be null");
        if (user.getUserName() == null || user.getUserName().isBlank())
            throw new IllegalArgumentException("User name cannot be blank");
        if (existingUser != null) {
            throw new IllegalArgumentException("User with this name already exists");
        }
    }

    private void validateEmail(String email, User user) {
        for (User k : repository.values()){
            if (Objects.equals(k.getUserMail(), user.getUserMail())){
                throw new IllegalArgumentException("User With This Email Already Exists");
            }
        }
        repository.get(user.getUserMail());
    }

    @Override
    public void store(User user) {
        validateEmail(user.getUserMail(), user);
        validateUser(user);

        repository.put(user.getUserName().toLowerCase(), user);
    }

    @Override
    public User retrieve(String name) {
        return repository.get(name);
    }

    @Override
    public Collection<User> returnAll() {
        return repository.values();
    }

    @Override
    public User delete(String name) {
        return repository.remove(name);
    }
}
