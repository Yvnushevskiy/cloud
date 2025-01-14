package com.example.demo.config;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.messages.Bucket;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.couchbase.CouchbaseProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.swing.*;

@RequiredArgsConstructor
@Configuration
public class MinioConfig {

    private final Environment environment;

    @Bean
    public MinioClient MinioClientBuilder() {
         return MinioClient.builder()
                .endpoint(environment.getProperty("MINIO_URL"))
                .credentials(environment.getProperty("MINIO_ROOT_USER"), environment.getProperty("MINIO_ROOT_PASSWORD"))
                .build();
    }

//    @PostConstruct
//    public void minioBucketBuilder(MinioClient minioClient)  {
//        try {
//            boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket("user-files").build());
//            if (!found) {
//                minioClient.makeBucket(MakeBucketArgs.builder().bucket("user-files").build());
//            }
//        }
//        catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }

}
