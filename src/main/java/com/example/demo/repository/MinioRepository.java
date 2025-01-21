package com.example.demo.repository;

import io.minio.Result;
import io.minio.messages.Item;
import org.springframework.web.multipart.MultipartFile;

public interface MinioRepository {
    void uploadFile(MultipartFile file, String path);
    void uploadMultipleFiles(MultipartFile[] files, String path);
    void deleteObject(String path, String objectName);
    void createFolder(String path,String folderName);
    Iterable<Result<Item>> buildFileObjectByPath(String path);
}
