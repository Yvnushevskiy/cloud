package com.example.demo.util;

import com.example.demo.model.FileObjectDTO;

import java.util.ArrayList;
import java.util.List;

import static com.example.demo.util.Util.extractFileNameFromPath;

public class FileObjectUtil {

    public static FileObjectDTO searchFilesByNameInFileObject(FileObjectDTO fileObjectDTO, String searchQuery){

        List<String> foundedFiles = new ArrayList<>();
        List<String> foundedFolders = new ArrayList<>();

        for (String foundedFile : fileObjectDTO.getFiles()) {
            if(searchQuery.equals(extractFileNameFromPath(foundedFile))){
                foundedFiles.add(foundedFile);
            }
        }
        for (String foundedFolder : fileObjectDTO.getFolder()) {
            if(searchQuery.equals(extractFileNameFromPath(foundedFolder))){
                foundedFolders.add(foundedFolder);
            }
        }
        fileObjectDTO.setFiles(foundedFiles);
        fileObjectDTO.setFolder(foundedFolders);
        return fileObjectDTO;
    }
    public static boolean doesPathContainSimilarName(FileObjectDTO fileObjectDTO, String searchQuery){
        return doesPathContainSimilarName(fileObjectDTO,searchQuery,false);
    }

    public static boolean doesPathContainSimilarName(FileObjectDTO fileObjectDTO, String searchQuery, boolean isItFolder){
        List<String> items = isItFolder ? fileObjectDTO.getFolder() : fileObjectDTO.getFiles();
        for (String s : items) {
            if(s.contains(searchQuery)) return true;
        }
        return false;
    }
}
