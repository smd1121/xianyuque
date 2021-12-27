package com.xuaninsr.xianyuque.mapper;

import com.xuaninsr.xianyuque.pojo.FileInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FileInfoMapper {
    void insert(FileInfo fileInfo);
    FileInfo selectByID(int ID);
    List<FileInfo> selectAll();
}
