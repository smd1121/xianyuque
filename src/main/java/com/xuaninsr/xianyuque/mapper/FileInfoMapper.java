package com.xuaninsr.xianyuque.mapper;

import com.xuaninsr.xianyuque.pojo.Article;
import com.xuaninsr.xianyuque.pojo.FileInfo;
import org.apache.ibatis.annotations.Mapper;

import java.sql.Timestamp;
import java.util.List;

@Mapper
public interface FileInfoMapper {
    void insert(FileInfo fileInfo);
    FileInfo selectByID(int ID);
    List<FileInfo> selectAll();
    List<FileInfo> selectAllTopLevelByUser(String userID);
    List<FileInfo> selectSonVisibleForUser(int fileID, String userID);
    int getLargestID();
    void deleteByID(int ID);
    void updateFile(int ID, String title, String content, Timestamp lastEdit);
    FileInfo getCache(int ID);
    FileInfo getActual(int ID);
    void insertPrivilege(int fileID, String userID);
    void moveTo(int son, String father, boolean isTopLev);
}
