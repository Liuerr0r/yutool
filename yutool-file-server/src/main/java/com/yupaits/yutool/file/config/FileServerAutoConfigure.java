package com.yupaits.yutool.file.config;

import com.github.tobato.fastdfs.FdfsClientConfig;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.yupaits.yutool.file.core.DownloadTemplate;
import com.yupaits.yutool.file.core.UploadTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.context.annotation.Import;
import org.springframework.jmx.support.RegistrationPolicy;

/**
 * 文件服务自动装配
 * @author yupaits
 * @date 2019/7/22
 */
@Import(FdfsClientConfig.class)
@EnableMBeanExport(registration = RegistrationPolicy.IGNORE_EXISTING)
@Configuration
public class FileServerAutoConfigure {

    @Bean
    @ConditionalOnMissingBean
    public UploadTemplate uploadTemplate(FastFileStorageClient storageClient) {
        return new UploadTemplate(storageClient);
    }

    @Bean
    @ConditionalOnMissingBean
    public DownloadTemplate downloadTemplate(FastFileStorageClient storageClient) {
        return new DownloadTemplate(storageClient);
    }
}
