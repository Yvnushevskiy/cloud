package com.example.demo.service;

import com.example.demo.Util.Util;
import com.example.demo.model.FileObject;
import com.example.demo.repository.MinioRepository;
import com.example.demo.repository.UserRepository;
import io.minio.MinioClient;
import io.minio.Result;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MinioService {

    private final UserRepository userRepository;
    private final MinioRepository minioRepository;

    private final MinioClient minioClient;
    private final UserService userService;

    @Value("${MINIO_BUCKET_NAME}")
    private  String minioBucketName;


    public void uploadFile(MultipartFile file, String username,String path) {
        minioRepository.loadFile(file,buildFullPath(username,path));
    }

    public FileObject getFileObjectForUser(String username,String path) {
        try {
            Iterable<Result<Item>> results = minioRepository.buildFileObjectByPath(buildFullPath(username,path));
            return mapperFromIterableToFileObj(results);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void createFolderOnPath(String path,String username,String folderName) {
        minioRepository.createFolder(buildFullPath(username,path),folderName);
    }




    private  String buildFullPath(String username, String path) {
        try {
            if(path==null) {
                path="";
            }
            String userFolder = buildUserFolderNameWithFoundedID(username);

            return userFolder + "/" + URLDecoder.decode(path, "UTF-8");

        }catch (Exception e){
            throw new RuntimeException("cant decode"+ e);
        }
    }

    private  String buildUserFolderNameWithFoundedID(String username){
        return "user-" + userRepository.findIdByUsername(username).toString() + "-files";
    }

    private String pathNameCutter(String fullPath, String userFolderName, String path) {
        return fullPath
                .replace(userFolderName+"/","")
                .replace(path,"");

    }

    private FileObject mapperFromIterableToFileObj(Iterable<Result<Item>> list) throws Exception{
        FileObject fileObject = new FileObject();
        List<String> folders = new ArrayList<>();
        List<String> files = new ArrayList<>();

        for (Result<Item> item : list) {
            if (item.get().isDir()) {
                folders.add(Util.cutPathFromFileName(item.get().objectName()));
            } else {
                files.add(Util.cutPathFromFileName(item.get().objectName()));
            }
        }
        fileObject.setFiles(files);
        fileObject.setFolder(folders);

        return fileObject;
    }


}



