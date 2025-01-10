package com.example.demo.controller;

import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @Autowired
    private UserService userService;


    @GetMapping("/login")
    public String loginPage(@RequestParam(value = "error",required = false)String error) {
        if(error != null) {
            return "sign-in-with-errors";
        }
        return "sign-in";
    }

    @PostMapping("/register")
    public String addUser(@RequestParam String username, @RequestParam String password) {
        userService.addUser(username, password);
        return "sign-in";
    }


    @GetMapping("/register")
    public String registerPage(@RequestParam(value = "error",required = false)String error) {
        if(error != null) {
            return "sign-up-with-errors";
        }
        return "sign-up";
    }
}
