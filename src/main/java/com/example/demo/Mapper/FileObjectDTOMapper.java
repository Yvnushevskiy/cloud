package com.example.demo.Mapper;

import com.example.demo.model.FileObjectDTO;
import io.minio.Result;
import io.minio.messages.Item;

import java.util.ArrayList;
import java.util.List;

public class FileObjectDTOMapper {
    public static FileObjectDTO map(Iterable<Result<Item>> list) {
        try {
            FileObjectDTO fileObjectDTO = new FileObjectDTO();
            List<String> folders = new ArrayList<>();
            List<String> files = new ArrayList<>();

            for (Result<Item> item : list) {
                if (item.get().isDir()) {
                    folders.add(item.get().objectName());
                } else if (!item.get().objectName().endsWith("/")) {
                    files.add(item.get().objectName());
                }
            }
            fileObjectDTO.setFiles(files);
            fileObjectDTO.setFolder(folders);
            return fileObjectDTO;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
