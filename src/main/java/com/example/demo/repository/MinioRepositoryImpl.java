package com.example.demo.repository;

import io.minio.*;
import io.minio.messages.Item;
import io.minio.CopyObjectArgs;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;


@Repository
@RequiredArgsConstructor
public class MinioRepositoryImpl implements MinioRepository {

    private final MinioClient minioClient;
    private final Environment env;


    public void uploadFile(MultipartFile file, String path) {
        try (InputStream inputStream = file.getInputStream()) {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(env.getProperty("MINIO_BUCKET_NAME"))
                            .object(path + file.getOriginalFilename())
                            .stream(inputStream, file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void uploadMultipleFiles(MultipartFile[] files, String path) {
        for (MultipartFile file : files) {
            uploadFile(file, path);
        }
    }

    public void deleteObject(String path) {
        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(env.getProperty("MINIO_BUCKET_NAME"))
                            .object(path)
                            .build());
        } catch (Exception e) {
            throw new RuntimeException("Cant remove file", e);
        }
    }

    public void createFolder(String path, String folderName) {
        try {
            System.out.println(path);
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(env.getProperty("MINIO_BUCKET_NAME"))
                            .object(path + folderName + "/")
                            .stream(new ByteArrayInputStream(new byte[0]), 0, -1)
                            .build());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public Iterable<Result<Item>> buildFileObjectByPath(String path) {
        return buildFileObjectByPath(path,false);
    }

    public Iterable<Result<Item>> buildFileObjectByPath(String path, boolean recursive) {
        return minioClient.listObjects(
                ListObjectsArgs.builder()
                        .bucket(env.getProperty("MINIO_BUCKET_NAME"))
                        .prefix(path)
                        .recursive(recursive)
                        .build());
    }

    public void copyObject(String path, String newPath){
        try {
            minioClient.copyObject(
                    CopyObjectArgs.builder()
                            .bucket(env.getProperty("MINIO_BUCKET_NAME"))
                            .object(newPath)
                            .source(CopySource.builder()
                                    .bucket(env.getProperty("MINIO_BUCKET_NAME"))
                                    .object(path)
                                    .build())
                            .build()
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}