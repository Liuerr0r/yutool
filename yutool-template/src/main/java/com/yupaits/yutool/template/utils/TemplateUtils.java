package com.yupaits.yutool.template.utils;

import com.yupaits.yutool.template.exception.TemplateException;
import com.yupaits.yutool.template.support.TempEngine;
import com.yupaits.yutool.template.support.TempEngineHelper;
import freemarker.template.Template;

import java.io.*;
import java.util.Map;

/**
 * 模板工具类
 * @author yupaits
 * @date 2019/7/22
 */
public class TemplateUtils {

    /**
     * 模板引擎生成字符串
     * @param tempDir 模板文件目录
     * @param tempFile 模板文件名
     * @param params 模板参数
     * @return 生成的字符串
     * @throws IOException 抛出IOException
     * @throws TemplateException 抛出TemplateException
     * @throws freemarker.template.TemplateException 抛出freemarker的TemplateException
     */
    public static String processToString(String tempDir, String tempFile, Map<String, Object> params)
            throws IOException, TemplateException, freemarker.template.TemplateException {
        return processToString(TempEngine.FREEMARKER, tempDir, tempFile, params);
    }

    /**
     * 模板引擎生成字符串
     * @param engine 模板引擎类型
     * @param tempDir 模板文件目录
     * @param tempFile 模板文件名
     * @param params 模板参数
     * @return 生成的字符串
     * @throws IOException 抛出IOException
     * @throws TemplateException 抛出TemplateException
     * @throws freemarker.template.TemplateException 抛出freemarker的TemplateException
     */
    public static String processToString(TempEngine engine, String tempDir, String tempFile, Map<String, Object> params)
            throws IOException, TemplateException, freemarker.template.TemplateException {
        String result;
        Template template = TempEngineHelper.getTemplate(engine, tempDir, tempFile);
        try (StringWriter writer = new StringWriter()) {
            template.process(params, writer);
            result = writer.toString();
        }
        return result;
    }

    /**
     * 模板引擎生成文件，默认覆盖已存在文件
     * @param tempDir 模板文件目录
     * @param tempFile 模板文件名
     * @param params 模板参数
     * @param filePath 目标文件路径
     * @throws IOException 抛出IOException
     * @throws TemplateException 抛出TemplateException
     * @throws freemarker.template.TemplateException 抛出freemarker的TemplateException
     */
    public static void writeToFile(String tempDir, String tempFile, Map<String, Object> params, String filePath)
            throws TemplateException, IOException, freemarker.template.TemplateException {
        writeToFile(TempEngine.FREEMARKER, tempDir, tempFile, params, filePath, true);
    }

    /**
     * 模板引擎生成文件，手动设置是否覆盖已存在文件
     * @param engine 模板引擎类型
     * @param tempDir 模板文件目录
     * @param tempFile 模板文件名
     * @param params 模板参数
     * @param filePath 目标文件路径
     * @throws IOException 抛出IOException
     * @throws TemplateException 抛出TemplateException
     * @throws freemarker.template.TemplateException 抛出freemarker的TemplateException
     */
    public static void writeToFile(TempEngine engine, String tempDir, String tempFile, Map<String, Object> params, String filePath, boolean overwrite)
            throws IOException, TemplateException, freemarker.template.TemplateException {
        checkAndCreateFile(filePath, overwrite);
        Template template = TempEngineHelper.getTemplate(engine, tempDir, tempFile);
        try (Writer writer = new FileWriter(filePath); BufferedWriter bufferedWriter = new BufferedWriter(writer)) {
            template.process(params, bufferedWriter);
            bufferedWriter.flush();
        }
    }

    /**
     * 字符串写入文件，默认覆盖已存在文件
     * @param str 待写入的字符串
     * @param filePath 目标文件路径
     * @throws IOException 抛出IOException
     */
    public static void stringToFile(String str, String filePath) throws IOException {
        stringToFile(str, filePath, true);
    }

    /**
     * 字符串写入文件，设置是否覆盖已存在文件
     * @param str 待写入的字符串
     * @param filePath 目标文件路径
     * @param overwrite 是否覆盖已存在文件
     * @throws IOException 抛出IOException
     */
    public static void stringToFile(String str, String filePath, boolean overwrite) throws IOException {
        checkAndCreateFile(filePath, overwrite);
        try (Writer writer = new FileWriter(filePath); BufferedWriter bufferedWriter = new BufferedWriter(writer)) {
            bufferedWriter.write(str);
        }
    }

    /**
     * 检查并创建文件
     * @param filePath 目标文件路径
     * @param overwrite 是否覆盖已存在文件
     * @throws IOException 抛出IOException
     */
    private static void checkAndCreateFile(String filePath, boolean overwrite) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            File parentDir = file.getParentFile();
            if (!parentDir.exists()) {
                if (!parentDir.mkdirs()) {
                    throw new IOException(String.format("创建目录 %s 失败", parentDir.getAbsolutePath()));
                }
            }
            if (!file.createNewFile()) {
                throw new IOException(String.format("创建文件 %s 失败", file.getAbsolutePath()));
            }
        } else if (!overwrite) {
            throw new IOException(String.format("文件 %s 已存在", filePath));
        }
    }
}
