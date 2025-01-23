package com.example.demo.util;

import java.net.URLDecoder;

public class Util {

    public static String getNameFromPath(String filePath) {
        if (filePath.endsWith("/")) {
            filePath = filePath.substring(0, filePath.length() - 1);
        }

        int lastSlashIndex = filePath.lastIndexOf("/");
        if (lastSlashIndex != -1) {
            return filePath.substring(lastSlashIndex + 1);
        }
        return filePath;
    }
    public static String cutFileType(String fileName){
        return fileName.contains(".") ? fileName.substring(0, fileName.lastIndexOf(".")): fileName;

    }
    public static String urlCutter(String Url) {
        return Url.replaceFirst(".*?/home", "/home");
    }

}