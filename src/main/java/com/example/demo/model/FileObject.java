package com.example.demo.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FileObject {
    private List<String> files;
    private List<String> folder;
}
