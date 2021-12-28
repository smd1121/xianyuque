package com.xuaninsr.xianyuque.utils;

import org.springframework.util.DigestUtils;

public class MD5Util {
    private static final String slat = "&%xianyuxuan*&%$#@";
    public static String getMD5(String str) {
        String base = str +"/"+slat;
        String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
        return md5;
    }
}
