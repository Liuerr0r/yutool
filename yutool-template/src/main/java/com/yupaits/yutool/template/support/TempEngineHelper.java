package com.yupaits.yutool.template.support;

import com.yupaits.yutool.template.exception.TemplateException;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;
import org.springframework.util.ClassUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 模板引擎辅助类
 * @author yupaits
 * @date 2019/7/22
 */
@SuppressWarnings("ConstantConditions")
public class TempEngineHelper {

    private static final Configuration FM_CONFIG;
    private static final String FM_TEMPLATE_PATH;

    static {
        Configuration freemarkerConfig = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
        freemarkerConfig.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        freemarkerConfig.setDefaultEncoding(StandardCharsets.UTF_8.name());
        FM_CONFIG = freemarkerConfig;
        FM_TEMPLATE_PATH = ClassUtils.getDefaultClassLoader().getResource("").getPath();
    }

    /**
     * 获取模板对象
     * @param tempDir 模板文件目录
     * @param tempFile 模板文件名
     * @param <T> 模板对象类型
     * @return 模板对象
     * @throws IOException 抛出IOException
     * @throws TemplateException 抛出TemplateException
     */
    public static <T> T getTemplate(String tempDir, String tempFile) throws IOException, TemplateException {
        return getTemplate(TempEngine.FREEMARKER, tempDir, tempFile);
    }

    /**
     * 获取模板对象
     * @param engine 模板引擎类型
     * @param tempDir 模板文件目录
     * @param tempFile 模板文件名
     * @param <T> 模板对象类型
     * @return 模板对象
     * @throws IOException 抛出IOException
     * @throws TemplateException 抛出TemplateException
     */
    @SuppressWarnings("unchecked")
    public static <T> T getTemplate(TempEngine engine, String tempDir, String tempFile) throws IOException, TemplateException {
        switch (engine) {
            case FREEMARKER:
                FM_CONFIG.setDirectoryForTemplateLoading(new File(FM_TEMPLATE_PATH, tempDir));
                Template template = FM_CONFIG.getTemplate(tempFile);
                return (T) template;
            case VELOCITY:
            case THYMELEAF:
            default:
                throw new TemplateException(String.format("不支持的模板引擎: %s", engine));
        }
    }
}
