package com.xuaninsr.xianyuque.service.impl;

import com.xuaninsr.xianyuque.mapper.UserMapper;
import com.xuaninsr.xianyuque.pojo.User;
import com.xuaninsr.xianyuque.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserServiceImpl implements UserService {
    @Resource
    private UserMapper userMapper;

    public void insertUser(User user) {
        userMapper.insert(user);
    }

    public User selectUserByID(String ID) {
        return userMapper.selectByID(ID);
    }
}
