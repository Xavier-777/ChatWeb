package com.mei.utils;

import com.mei.constant.PathConst;
import com.mei.enums.MessageType;

import java.io.File;

public class FileUtils {

    /**
     * 文件系统初始化：创建装上传文件的文件夹，并保存该文件夹的相关信息
     *
     * @param fileDir
     * @return
     */
    public static int FileSystemInit(String fileDir) {
        File uploadDir = new File(fileDir);
        uploadDir.mkdir();
        PathConst.uploadPath = uploadDir.toPath();
        return 1;
    }

    /**
     * 删除目标目录下的所有文件
     *
     * @param dirPath
     * @return
     */
    public static boolean deleteAll(String dirPath) {
        File dir = new File(dirPath);       //获取文件夹
        File[] files = dir.listFiles();
        if (files.length == 0)      //没有文件
            return false;
        for (File file : files) {
            file.delete();
        }
        return true;
    }

    /**
     * 输入文件的后缀名，获取对应的messageType
     *
     * @param suffix
     * @return
     */
    public static MessageType getFileType(String suffix) {
        if (".dvi .mp4".contains(suffix.toLowerCase()))
            return MessageType.Video;
        else if(".png .jpg .jpeg".contains(suffix.toLowerCase()))
            return MessageType.Image;
        else if(".mp3".contains(suffix.toLowerCase()))
            return MessageType.Audio;
        else
            return MessageType.Other;
    }
}
