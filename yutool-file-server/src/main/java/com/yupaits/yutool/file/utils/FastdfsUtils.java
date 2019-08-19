package com.yupaits.yutool.file.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * Fastdfs工具类
 * @author yupaits
 * @date 2019/7/23
 */
public class FastdfsUtils {
    private static final String PATH_SEPARATOR = "/";

    /**
     * 从文件全路径获取分组名称
     * @param fullPath 全路径
     * @return 分组名称
     */
    public static String getGroupName(String fullPath) {
        String groupName = null;
        if (StringUtils.isNotBlank(fullPath)) {
            groupName = StringUtils.substringBefore(fullPath, PATH_SEPARATOR);
        }
        return groupName;
    }

    /**
     * 从文件全路径获取文件path
     * @param fullPath 全路径
     * @return 文件path
     */
    public static String getPath(String fullPath) {
        String path = null;
        if (StringUtils.isNotBlank(fullPath)) {
            path = StringUtils.substringAfter(fullPath, PATH_SEPARATOR);
        }
        return path;
    }
}
