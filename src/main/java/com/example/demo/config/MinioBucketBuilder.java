package com.example.demo.config;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MinioBucketBuilder {

    private final MinioClient minioClient;
    private final Environment environment;

    @PostConstruct
    public void minioBucketBuilder()  {
        try {
            boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(environment.getProperty("MINIO_BUCKET_NAME")).build());
            if (!found) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(environment.getProperty("MINIO_BUCKET_NAME")).build());
            }
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
