package com.productpulse.productpulse.controller;

import com.productpulse.productpulse.model.Users;
import com.productpulse.productpulse.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Autowired
    private UserRepo userRepo;

    @PostMapping("/register")
    public String register(@RequestBody Users user) {
        System.out.println("REGISTERED PASSWORD: " + user.getPassword());
        return userRepo.save(user).getUsername() + " registered successfully!";
    }

    @GetMapping("/")
    public String greet() {
        return "You are authenticated!";
    }
}
