package com.example.demo.repository;

import io.minio.Result;
import io.minio.messages.Item;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;
@Repository
public interface MinioRepository {
    void loadFile(MultipartFile file, String path);
    void loadFolder();
    void deleteFolder();
    void deleteFile();
    void createFolder();
    Iterable<Result<Item>> buildFileObjectByPath(String path);
}
