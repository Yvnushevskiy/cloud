package com.example.demo.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {


    @GetMapping("/home")
    public String addUser(Authentication authentication, Model model) {
        model.addAttribute("username", authentication.getName());
        return "index";
    }
}
