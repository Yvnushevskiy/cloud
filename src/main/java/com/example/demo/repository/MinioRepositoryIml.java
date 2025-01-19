package com.example.demo.repository;

import com.example.demo.model.FileObject;
import io.minio.ListObjectsArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.Result;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.web.multipart.MultipartFile;
import java.io.InputStream;


@RequiredArgsConstructor
public class MinioRepositoryIml implements MinioRepository {

    private final MinioClient minioClient;
    private final Environment env;


    public void loadFile(MultipartFile file, String path) {
        try (InputStream inputStream = file.getInputStream()) {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(env.getProperty("MINIO_BUCKET_NAME"))
                            .object(path)
                            .stream(inputStream, file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public void loadFolder() {

    }


    public void deleteFolder() {

    }


    public void deleteFile() {

    }


    public void createFolder() {

    }

    public Iterable<Result<Item>> buildFileObjectByPath(String path) {
        Iterable<Result<Item>> results = minioClient.listObjects(
                ListObjectsArgs.builder()
                        .bucket(env.getProperty("MINIO_BUCKET_NAME"))
                        .prefix(path)
                        .build());

        return results;
    }
}
