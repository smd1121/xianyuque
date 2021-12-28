package com.xuaninsr.xianyuque.mapper;

import com.xuaninsr.xianyuque.pojo.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    void insert(User user);
    User selectByID(String ID);
}
