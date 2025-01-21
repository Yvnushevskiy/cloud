package com.example.demo.Util;

public class Util {

    public static String buildUserFolderName(int userID) {
        return "user-"+userID+"-files";
    }
    public static String cutPathFromFileName(String filePath) {
        return filePath.substring(filePath.lastIndexOf("/")+1);
    }
    public static String getFolderNameFromPath(String filePath, String fullPath) {
        String folderName = filePath.substring(fullPath.length());

        if (folderName.endsWith("/")) {
            folderName = folderName.substring(0, folderName.length() - 1);
        }

        return folderName;
    }
}