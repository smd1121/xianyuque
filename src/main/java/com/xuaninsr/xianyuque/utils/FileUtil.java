package com.xuaninsr.xianyuque.utils;

import com.xuaninsr.xianyuque.pojo.FileInfo;
import org.thymeleaf.util.StringUtils;

public class FileUtil {
    public static String getContent(FileInfo fileInfo) {
        if (fileInfo == null
                || StringUtils.isEmptyOrWhitespace(fileInfo.getLocalName()))
            return "";

        return fileInfo.getLocalName();
    }
}
