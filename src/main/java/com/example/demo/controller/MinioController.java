package com.example.demo.controller;

import com.example.demo.model.FileObjectDTO;
import com.example.demo.service.MinioService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.*;

@Controller
@RequiredArgsConstructor
public class MinioController {


    private final MinioService minioService;


    @GetMapping("/home/**")
    public String homePage(Authentication authentication, Model model, HttpServletRequest request) {
        FileObjectDTO fObj = minioService.getFileObjectForPath(authentication.getName(),request.getRequestURI());
        model.addAttribute("username", authentication.getName());
        model.addAttribute("files", fObj.getFiles());
        model.addAttribute("folders", fObj.getFolder());
        return "index";
    }


    @PostMapping("/upload")
    public String uploadFile(Authentication authentication, Model model,@RequestParam("file") MultipartFile file,HttpServletRequest request){
        String previousUrl = request.getHeader("Referer");
        minioService.uploadFile(file,authentication.getName(),previousUrl);
        return "redirect:"+previousUrl;
    }

    @PostMapping("/upload-folder")
    public String uploadFolder(Authentication authentication,@RequestParam("files") MultipartFile[] files, Model model,HttpServletRequest request){
        String previousUrl = request.getHeader("Referer");
        minioService.uploadFolder(files,authentication.getName(),previousUrl);
        return "redirect:"+previousUrl;
    }

    @PostMapping("/create-folder")
    public String createFolder(Authentication authentication,Model model,HttpServletRequest request,String folderName){
        String previousUrl = request.getHeader("Referer");
        minioService.createFolder(previousUrl,authentication.getName(),folderName);
        return "redirect:"+previousUrl;
    }

    @PostMapping("/delete")
    public String deleteFile(Authentication authentication,Model model,@RequestParam("path") String path){
        minioService.deleteFile(path);
        return "redirect:/home";
    }

    @PostMapping("/delete-folder")
    public String deleteFolder(Authentication authentication,Model model,@RequestParam("path") String path) {
        minioService.deleteFolder(path);
        return "redirect:/home";
    }

    @PostMapping("/rename")
    public String renameFile(HttpServletRequest request,@RequestParam("newName") String newName,@RequestParam("path") String path){
        String previousUrl = request.getHeader("Referer");
        minioService.renameFile(path,newName);
        return "redirect:"+previousUrl;
    }
    @PostMapping("/rename-folder")
    public String renameFolder(HttpServletRequest request,@RequestParam("newName") String newName,@RequestParam("path") String path){
        String previousUrl = request.getHeader("Referer");
        minioService.renameFolder(path,newName);
        return "redirect:"+previousUrl;
    }

    @GetMapping("/search")
    public String searchedFile(Authentication authentication,@RequestParam("query") String query,Model model){
        FileObjectDTO fObj = minioService.searchFilesByNameInUserFolder(authentication.getName(),query);
        model.addAttribute("username", authentication.getName());
        model.addAttribute("files", fObj.getFiles());
        model.addAttribute("folders", fObj.getFolder());
        return "search";
    }
    @PostMapping("/download")
    public String downloadFile(String localPath, Model model, @RequestParam("path") String path){
        minioService.downloadFile(path);
        return "redirect:/home";
    }
    @PostMapping("/download-folder")
    public String downloadFolder(String localPath,Model model,@RequestParam("path") String path){
        minioService.downloadFolder(path);
        return "redirect:/home";
    }
}


