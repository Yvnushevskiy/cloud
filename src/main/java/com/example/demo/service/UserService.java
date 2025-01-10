package com.example.demo.service;

import com.example.demo.model.AppUser;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void addUser(String username, String password) {
        String encodedPassword = passwordEncoder.encode(password);
        AppUser user = new AppUser();
        user.setUsername(username);
        user.setPassword(encodedPassword);
        userRepository.save(user);
    }


}
