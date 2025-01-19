package com.example.demo.config;

import io.minio.MinioClient;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
@RequiredArgsConstructor
public class MinioConfig {

    private final Environment environment;


    @Bean
    public MinioClient MinioClientBuilder() {
        return MinioClient.builder()
                .endpoint(environment.getProperty("MINIO_URL"))
                .credentials(environment.getProperty("MINIO_ROOT_USER"), environment.getProperty("MINIO_ROOT_PASSWORD"))
                .build();
    }
}

