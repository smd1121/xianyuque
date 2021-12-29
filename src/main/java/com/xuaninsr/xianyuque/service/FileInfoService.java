package com.xuaninsr.xianyuque.service;

import com.xuaninsr.xianyuque.pojo.FileInfo;

import java.util.List;

public interface FileInfoService {
    void insertFileInfo(FileInfo fileInfo);
    FileInfo selectFileInfoByID(int ID);
    List<FileInfo> selectAllFileInfo();
    List<FileInfo> selectAllTopLevelByUser(String userID);
    List<FileInfo> selectSonVisibleForUser(int fileID, String userID);
    int getLargestFileID();
    void deleteFileByID(int ID);
}
