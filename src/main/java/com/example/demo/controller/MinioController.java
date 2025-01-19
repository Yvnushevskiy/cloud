package com.example.demo.controller;

import com.example.demo.config.MinioConfig;
import com.example.demo.model.FileObject;
import com.example.demo.service.MinioService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequiredArgsConstructor
public class MinioController {


    private final MinioService minioService;


    @GetMapping("/home")
    public String homePage(Authentication authentication, Model model,@RequestParam(value="path",required = false) String path) {
        String username = authentication.getName();
        FileObject fObj = minioService.getFileObjectForUser(username,path);
        model.addAttribute("username", username);
        model.addAttribute("files", fObj.getFiles());
        model.addAttribute("folders", fObj.getFolder());
        return "index";
    }


    @PostMapping("/upload")
    public String uploadFile(Authentication authentication, Model model,@RequestParam("file") MultipartFile file,@RequestParam(value="path",required = false) String path){
        minioService.uploadFile(file,authentication.getName(),path);
        return "index";
    }

}


