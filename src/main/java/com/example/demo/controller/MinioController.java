package com.example.demo.controller;

import com.example.demo.config.MinioConfig;
import com.example.demo.service.MinioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class MinioController {

    @Autowired
    private MinioService minioService;


    @GetMapping("/home")
    public String addUser(Authentication authentication, Model model) {
        model.addAttribute("username", authentication.getName());
        return "index";
    }


    @PostMapping("/upload")
    public void uploadFile(Authentication authentication, Model model,@RequestParam MultipartFile file) throws Exception {
        model.addAttribute("username", authentication.getName());
        minioService.uploadFile(file);
    }





//    @PostMapping
//    public void loadFile(Authentication authentication, Model model,@RequestParam MultipartFile file) {
//        minioService.XuyatinaPolnaya("aga",file.getInputStream(),file.getOriginalFilename());
//    }
}


