package com.example.demo.service;

import com.example.demo.Util.Util;
import com.example.demo.model.FileObject;
import com.example.demo.repository.MinioRepository;
import com.example.demo.repository.UserRepository;
import io.minio.ListObjectsArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.Result;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
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

        String userFolderName = buildUserFolderNameWithFoundedID(username);
        String fullPath = minioUrlBuilder(userFolderName,path);

        minioRepository.loadFile(file,fullPath);
    }

    public FileObject getFileObjectForUser(String username,String path) {
        try {
            String userFolderName = buildUserFolderNameWithFoundedID(username);
            String fullPath = minioUrlBuilder(userFolderName,path);

            Iterable<Result<Item>> results = minioRepository.buildFileObjectByPath(fullPath);
            return MapperFromIterableToFileObj(results);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



    private  String minioUrlBuilder(String userFolder,String path) {
        try {
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

    private FileObject MapperFromIterableToFileObj(Iterable<Result<Item>> list) throws Exception{
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



