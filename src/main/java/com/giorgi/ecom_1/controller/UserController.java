package com.giorgi.ecom_1.controller;

import com.giorgi.ecom_1.model.User;
import com.giorgi.ecom_1.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
public class UserController {
    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/users")
    @Operation(summary = "Show All Users")
    public Collection<User> getUsers(){
        return userRepository.returnAll();
    }

    @PostMapping("/register")
    @Operation(summary = "Register new account")
    public User addUser(@RequestBody User user){
        userRepository.store(user);
        return user;
    }
}
