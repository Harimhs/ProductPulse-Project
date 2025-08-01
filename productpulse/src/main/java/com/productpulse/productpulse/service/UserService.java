package com.productpulse.productpulse.service;

import com.productpulse.productpulse.model.Users;
import com.productpulse.productpulse.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepo repo;

    private BCryptPasswordEncoder bcode= new BCryptPasswordEncoder(12);

    public Users register(Users user) {
        user.setPassword(bcode.encode(user.getPassword()));
        return repo.save(user);
    }

    public String verify(Users user) {

    }
}
