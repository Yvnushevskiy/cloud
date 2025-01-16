package com.example.demo.service;

import com.example.demo.model.MinioFile;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.MinioException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Service
@RequiredArgsConstructor
public class MinioService {
    private final MinioClient minioClient;
    private final Environment environment;

    public void uploadFile(MultipartFile file) {
        try (InputStream inputStream = file.getInputStream()) {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(environment.getProperty("MINIO_BUCKET_NAME"))
                            .object(file.getName())
                            .stream(inputStream, file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



    public static MinioFile fromMultipartFileToMinioFile(MultipartFile multipartFile) {
        try {
            return new MinioFile(
                    multipartFile.getInputStream(),
                    multipartFile.getOriginalFilename(),
                    multipartFile.getContentType(),
                    multipartFile.getSize()
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}


//    public void uploadFolder(InputStream inputStream) {
//
//    }

