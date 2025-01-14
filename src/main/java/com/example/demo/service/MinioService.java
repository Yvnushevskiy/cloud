package com.example.demo.service;

import com.example.demo.model.MinioFile;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.MinioException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Service
@RequiredArgsConstructor
public class MinioService {
    private final MinioClient minioClient;
    private final String bucketName = "user-bucket";

    public void uploadFile(MultipartFile file) {
        try {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(file.getName())
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build());
                    file.getInputStream().close();
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

