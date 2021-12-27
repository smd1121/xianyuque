package com.xuaninsr.xianyuque.service;

import com.xuaninsr.xianyuque.pojo.FileInfo;

import java.util.List;

public interface FileInfoService {
    void insertFileInfo(FileInfo fileInfo);
    FileInfo selectFileInfoByID(int ID);
    List<FileInfo> selectAllFileInfo();
}
