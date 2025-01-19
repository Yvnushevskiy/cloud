package com.example.demo.Util;

public class Util {

    public static String buildUserFolderName(int userID) {
        return "user-"+userID+"-files";
    }
    public static String cutPathFromFileName(String filePath) {
        return filePath.substring(filePath.lastIndexOf("/")+1);
    }
}