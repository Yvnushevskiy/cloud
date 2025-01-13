package com.example.demo.config;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class MinioConfig {

    @Bean
    public MinioClient MinioClientBuilder() {
         return MinioClient.builder()
                .endpoint(System.getenv("MINIO_URL") + ":" + System.getenv("MINIO_PORT"))
                .credentials(System.getenv("MINIO_ROOT_USER"), System.getenv("MINIO_ROOT_PASSWORD"))
                .build();
    }


    @Bean
    public void MinioBucketBuilder(MinioClient minioClient) throws Exception {
        boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket("user-files").build());
        if (!found) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket("user-files").build());
        }
    }



}