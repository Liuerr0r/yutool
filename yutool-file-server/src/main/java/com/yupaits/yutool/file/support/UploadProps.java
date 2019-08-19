package com.yupaits.yutool.file.support;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

/**
 * 文件上传配置参数
 * @author yupaits
 * @date 2019/7/22
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UploadProps {

    /**
     * 文件存储Group，用于租户文件隔离
     */
    private String storeGroup;

    /**
     * 是否保存缩略图
     */
    private boolean thumb;

    /**
     * 缩略图宽度
     */
    private int width;

    /**
     * 缩略图高度
     */
    private int height;

    /**
     * 缩略图缩放百分比
     */
    private double percent;

    /**
     * 校验参数
     * @return 校验通过
     */
    public boolean isValid() {
        return StringUtils.isNotBlank(storeGroup) && (!thumb || ((width > 0 && height > 0) || percent > 0));
    }
}
