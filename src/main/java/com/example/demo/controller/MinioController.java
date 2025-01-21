package com.example.demo.controller;

import com.example.demo.model.FileObject;
import com.example.demo.service.MinioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.http.HttpRequest;

@Controller
@RequiredArgsConstructor
public class MinioController {


    private final MinioService minioService;


    @GetMapping("/home/**")
    public String homePage(Authentication authentication, Model model,@RequestParam(value="path",required = false) String path) {
        System.out.println("path");
        String username = authentication.getName();
        FileObject fObj = minioService.getFileObjectForUser(username,path);
        model.addAttribute("username", username);
        model.addAttribute("files", fObj.getFiles());
        model.addAttribute("folders", fObj.getFolder());
        return "index";
    }

//    @GetMapping("/home/{path}")
//    public String homePages(HttpServletRequest request,Authentication authentication, Model model, @PathVariable(required = false) String path) {
//        String username = authentication.getName();
//        FileObject fObj = minioService.getFileObjectForUser(username,path+"/");
//        model.addAttribute("username", username);
//        model.addAttribute("files", fObj.getFiles());
//        model.addAttribute("folders", fObj.getFolder());
//        return "index";
//    }


    @PostMapping("/upload")
    public String uploadFile(Authentication authentication, Model model,@RequestParam("file") MultipartFile file,@RequestParam(value="path",required = false) String path){
        minioService.uploadFile(file,authentication.getName(),path);
        return "redirect:/";
    }

    @PostMapping("/upload-folder")
    public String uploadFolder(Authentication authentication,@RequestParam("files") MultipartFile[] files, Model model,@RequestParam(value="path",required = false) String path){
        minioService.uploadFolder(files,authentication.getName(),path);
        return "index";
    }

//    @GetMapping("/open-file")
//    public String uploadFolder(Authentication authentication,@RequestParam("files") MultipartFile[] files, Model model,@RequestParam(value="path",required = false) String path){
//        minioService.uploadFolder(files,authentication.getName(),path);
//        return "index";
//    }


}


