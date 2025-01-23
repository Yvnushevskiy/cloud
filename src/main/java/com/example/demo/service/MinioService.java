package com.example.demo.service;

import com.example.demo.Mapper.FileObjectDTOMapper;
import com.example.demo.model.FileObjectDTO;
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

import static com.example.demo.util.Util.*;

@Service
@RequiredArgsConstructor
public class MinioService {

    private final UserRepository userRepository;
    private final MinioRepository minioRepository;

    private final MinioClient minioClient;
    private final UserService userService;

    @Value("${MINIO_BUCKET_NAME}")
    private String minioBucketName;


    public void uploadFile(MultipartFile file, String username, String path) {
        minioRepository.uploadFile(file, buildFullPath(username, urlCutter(path)));
    }

    public void uploadFolder(MultipartFile[] files, String username, String path) {
        minioRepository.uploadMultipleFiles(files, buildFullPath(username, urlCutter(path)));
    }

    public FileObjectDTO getFileObjectForUser(String username, String path) {
        try {
            String fullPath = buildFullPath(username, path);
            Iterable<Result<Item>> results = minioRepository.buildFileObjectByPath(fullPath);
            return FileObjectDTOMapper.map(results);
        } catch (Exception e) {
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
            Iterable<Result<Item>> results = minioRepository.buildFileObjectByPath(path,true);
            for (Result<Item> item : results) {
                deleteFile(item.get().objectName());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public void renameFile(String path, String newName) {
        minioRepository.copyObject(path,path.replace(cutFileType(getNameFromPath(path)),newName));
        minioRepository.deleteObject(path);
    }

    public void renameFolder(String path, String newName) {
      try {
          Iterable<Result<Item>> results = minioRepository.buildFileObjectByPath(path, true);
          for (Result<Item> item : results) {
              minioRepository.copyObject(item.get().objectName(),item.get().objectName().replace(getNameFromPath(path), newName));
              minioRepository.deleteObject(item.get().objectName());
          }
      }catch (Exception e){
          throw new RuntimeException(e);
      }
    }

    private String buildFullPath(String username, String path) {
        try {
            String userFolder = buildUserFolderNameWithFoundedID(username);
            String decodedPath = URLDecoder.decode(path, "UTF-8");
            String cutMainMageName = decodedPath.replaceFirst("^/[^/]+/", "").replaceFirst("^/[^/]+$", "");

            if (!cutMainMageName.isEmpty()) {
                cutMainMageName += "/";
            }

            return userFolder + "/" + cutMainMageName;

        } catch (Exception e) {
            throw new RuntimeException("cant decode" + e);
        }
    }

    private String buildUserFolderNameWithFoundedID(String username) {
        return "user-" + userRepository.findIdByUsername(username).toString() + "-files";
    }

}



