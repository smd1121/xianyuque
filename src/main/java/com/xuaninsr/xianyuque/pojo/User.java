package com.xuaninsr.xianyuque.pojo;

import com.xuaninsr.xianyuque.utils.MD5Util;
import org.springframework.lang.Nullable;

public class User {
    private String ID;
    private String password;
    @Nullable private String name;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = MD5Util.getMD5(password);;
    }

    @Nullable
    public String getName() {
        return name;
    }

    public void setName(@Nullable String name) {
        this.name = name;
    }
}
