package com.example.demo.service;

import com.example.demo.Mapper.FileObjectDTOMapper;
import com.example.demo.model.FileObjectDTO;
import com.example.demo.repository.MinioRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.util.Util;
import com.example.demo.util.FileObjectUtil;
import io.minio.Result;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

import static com.example.demo.util.Util.*;

@Service
@RequiredArgsConstructor
public class MinioService {

    private final UserRepository userRepository;
    private final MinioRepository minioRepository;


    public void uploadFile(MultipartFile file, String username, String path) {
        minioRepository.uploadFile(file, buildFullPath(username, urlCutter(path)));
    }

    public void uploadFolder(MultipartFile[] files, String username, String path) {
        minioRepository.uploadMultipleFiles(files, buildFullPath(username, urlCutter(path)));
    }
    public void downloadFile(String path) {
        minioRepository.downloadObject(path);
    }
    public void downloadFolder(String path) {
        try {
            Iterable<Result<Item>> results = minioRepository.findFilesByPath(path, true);
            for (Result<Item> item : results) {
                String objectName = item.get().objectName();

                File file = new File(path);
                file.getParentFile().mkdirs();

                minioRepository.downloadObject(objectName);
            }
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public void createFolder(String path, String username, String folderName) {
        minioRepository.createFolder(buildFullPath(username, urlCutter(path)), folderName);
    }

    public void deleteFile(String path) {
        minioRepository.deleteObject(path);
    }

    public void deleteFolder(String path) {
        try {
            Iterable<Result<Item>> results = minioRepository.findFilesByPath(path,true);
            for (Result<Item> item : results) {
                deleteFile(item.get().objectName());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public FileObjectDTO getFileObjectForPath(String username, String path) {
      return getFileObjectForPath(buildFullPath(username,path));
    }

    public FileObjectDTO getFileObjectForPath(String path) {
        try {
            Iterable<Result<Item>> results = minioRepository.findFilesByPath(path);
            FileObjectDTO fileObjectDTO = FileObjectDTOMapper.map(results);
            return FileObjectDTOMapper.map(results);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public FileObjectDTO searchFilesByNameInUserFolder(String username,String searchQuery){
        Iterable<Result<Item>> results = minioRepository.findFilesByPath(buildUserFolderNameWithFoundedID(username),true);
        FileObjectDTO fileObjectDTO = FileObjectDTOMapper.map(results);
        for (String file : fileObjectDTO.getFolder()) {
            System.out.println(file);
        }

        return FileObjectUtil.searchFilesByNameInFileObject(fileObjectDTO,searchQuery);
    }

    public void renameFile(String path, String newName) {
        try {
            String pathOfFile = getBasePath(path);

            if(FileObjectUtil.doesPathContainSimilarName(getFileObjectForPath(pathOfFile),newName)) return;

            copyAndDelete(path, path.replace(cutFileType(extractFileNameFromPath(path)), newName));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void renameFolder(String path, String newName) {
        try {
            String pathOfFolder = getBasePath(path);
            if(FileObjectUtil.doesPathContainSimilarName(getFileObjectForPath(pathOfFolder),newName,true)) return;
            Iterable<Result<Item>> results = minioRepository.findFilesByPath(path, true);


            for (Result<Item> item : results) {
                copyAndDelete(item.get().objectName(),item.get().objectName().replace(extractFileNameFromPath(path), newName));
            }
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }


    private void copyAndDelete(String oldPath, String newPath) {
        minioRepository.copyObject(oldPath, newPath);
        minioRepository.deleteObject(oldPath);
    }

   private String buildFullPath(String username,String path){
        return Util.buildFullPath(buildUserFolderNameWithFoundedID(username),path);
   }

    private String buildUserFolderNameWithFoundedID(String username) {
        return "user-" + userRepository.findIdByUsername(username).toString() + "-files";
    }

}



