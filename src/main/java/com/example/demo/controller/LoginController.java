package com.example.demo.controller;

import com.example.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.http.HttpRequest;

@RequiredArgsConstructor
@Controller
public class LoginController {

    private final UserService userService;
//todo VALIDATE!
    @GetMapping("/login")
    public String loginPage(@RequestParam(value = "error",required = false)String error) {
        return error!=null ? "sign-in-with-errors":"sign-in";
    }

    @PostMapping("/register")
    public String addUser(@RequestParam String username, @RequestParam String password) {
        userService.addUser(username, password);
        return "sign-in";
    }


    @GetMapping("/register")
    public String registerPage(@RequestParam(value = "error",required = false)String error) {
        return error != null ? "sign-up-with-errors":"sign-up";
    }
}
