package com.example.demo.util;

import java.net.URLDecoder;

public class Util {

    public static String getBasePath(String path) {
        return path.replace(extractFileNameFromPath(path), "");
    }


    public static String extractFileNameFromPath(String filePath) {
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

    public static String buildFullPath(String userFolder, String path) {
        try {
            String decodedPath = URLDecoder.decode(path, "UTF-8");
            String cutMainMageName = decodedPath.replaceFirst("^/[^/]+/", "").replaceFirst("^/[^/]+$", "");

            if (!cutMainMageName.isEmpty()) {
                cutMainMageName += "/";
            }

            return userFolder + "/" + cutMainMageName;

        } catch (Exception e) {
            throw new RuntimeException("cant decode" + e);
        }
    }

}