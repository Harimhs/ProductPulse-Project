package com.productpulse.productpulse.service;

import com.productpulse.productpulse.model.Users;
import com.productpulse.productpulse.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepo repo;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JWTService JWTService;

    private BCryptPasswordEncoder bcode= new BCryptPasswordEncoder(12);

    public Users register(Users user) {
        user.setPassword(bcode.encode(user.getPassword()));
        return repo.save(user);
    }

    public String verify(Users user) {
        Authentication authentication =
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));
        if(authentication.isAuthenticated())
            return JWTService.generateToken(user.getEmail());
        else return "Authentication Failed!";
    }
}
