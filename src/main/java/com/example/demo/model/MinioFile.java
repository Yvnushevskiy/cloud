package com.example.demo.model;

import lombok.Getter;
import lombok.Setter;

import java.io.InputStream;
@Getter
@Setter
public class MinioFile {
    //TODO This class is  useless, doesnt used anywhere
    InputStream inputStream;
    String fileName;
    String contentType;
    long size;

    public MinioFile(InputStream inputStream, String fileName, String contentType, long size) {
        this.inputStream = inputStream;
        this.fileName = fileName;
        this.contentType = contentType;
        this.size = size;
    }
}
