package com.xuaninsr.xianyuque.service;

import com.xuaninsr.xianyuque.pojo.User;

public interface UserService {
    void insertUser(User user);
    User selectUserByID(String ID);
    void updatePassword(String ID, String password);
    void updateName(String ID, String name);
}
