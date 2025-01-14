package com.example.demo.Minio;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MinioBucketBuilder {

    private final MinioClient minioClient;

    @PostConstruct
    public void minioBucketBuilder()  {
        try {
            boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket("user-files").build());
            if (!found) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket("user-files").build());
            }
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
