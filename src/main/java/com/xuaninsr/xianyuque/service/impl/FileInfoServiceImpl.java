package com.xuaninsr.xianyuque.service.impl;

import com.xuaninsr.xianyuque.mapper.FileInfoMapper;
import com.xuaninsr.xianyuque.pojo.Article;
import com.xuaninsr.xianyuque.pojo.FileInfo;
import com.xuaninsr.xianyuque.service.FileInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.List;

@Service
public class FileInfoServiceImpl implements FileInfoService {
    @Resource
    private FileInfoMapper fileInfoMapper;

    @Override
    public void insertFileInfo(FileInfo fileInfo) {
        fileInfoMapper.insert(fileInfo);
    }

    @Override
    public FileInfo selectFileInfoByID(int ID) {
        return fileInfoMapper.selectByID(ID);
    }

    @Override
    public List<FileInfo> selectAllFileInfo() {
        return fileInfoMapper.selectAll();
    }

    @Override
    public List<FileInfo> selectAllTopLevelByUser(String userID) {
        return fileInfoMapper.selectAllTopLevelByUser(userID);
    }

    @Override
    public List<FileInfo> selectSonVisibleForUser(int fileID, String userID) {
        return fileInfoMapper.selectSonVisibleForUser(fileID, userID);
    }

    public int getLargestFileID() {
        return fileInfoMapper.getLargestID();
    }

    public void deleteFileByID(int ID) {
        fileInfoMapper.deleteByID(ID);
    }

    public void updateFile(Article article) {
        if (article != null)
            fileInfoMapper.updateFile(article.getID(), article.getTitle(),
                    article.getContent(), new Timestamp(System.currentTimeMillis()));
    }

    public FileInfo getCache(int ID) {
        return fileInfoMapper.getCache(ID);
    }

    public FileInfo getActual(int ID) {
        return fileInfoMapper.getActual(ID);
    }
}
