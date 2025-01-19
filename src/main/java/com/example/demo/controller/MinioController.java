package com.example.demo.controller;

import com.example.demo.config.MinioConfig;
import com.example.demo.model.FileObject;
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
    public String addUser(Authentication authentication, Model model,@RequestParam(value="path",required = false) String path) {
        String username = authentication.getName();
        if(path == null){   //todo move it to service
            path = "";
        }
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


