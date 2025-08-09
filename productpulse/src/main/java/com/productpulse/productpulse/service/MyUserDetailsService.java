package com.productpulse.productpulse.service;

import com.productpulse.productpulse.model.UserPrincipal;
import com.productpulse.productpulse.model.Users;
import com.productpulse.productpulse.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepo repo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Users user= repo.findByUsername(email);
        if(user==null){
            throw new UsernameNotFoundException("User not Found!");
        }
        System.out.println("LOGIN PASSWORD FROM DB: " + user.getPassword());
        System.out.println("Loaded user: " + user.getEmail() + " with password: " + user.getPassword());
        return new UserPrincipal(user);
    }
}
