package com.yupaits.yutool.push.support;

/**
 * 消息模板接口
 * @author yupaits
 * @date 2019/7/19
 */
public interface IMsgTemplate {
    /**
     * 获取模板代码
     * @return 模板代码
     */
    int getCode();

    /**
     * 获取模板名称
     * @return 模板名称
     */
    String getName();

    /**
     * 获取模板文件路径
     * @return 模板文件路径
     */
    String getTemplateFile();

    /**
     * 获取模板文件目录
     * @return 模板文件目录
     */
    String getTemplateDir();

    /**
     * 获取模板文件名
     * @return 模板文件名
     */
    String getTemplateFilename();
}
