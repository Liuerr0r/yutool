package com.yupaits.yutool.file.support;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.coobird.thumbnailator.geometry.Position;
import net.coobird.thumbnailator.geometry.Positions;
import org.apache.commons.lang3.StringUtils;

/**
 * @author yupaits
 * @date 2019/7/23
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DownloadProps {

    /**
     * 是否下载缩略图
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
     * 图片质量（压缩比，如0.8表示压缩比为80%）
     */
    private float quality = 1.0f;

    /**
     * 图片缩放比例
     */
    private double scale = 1.0;

    /**
     * 是否附带水印
     */
    private boolean withWatermark;

    /**
     * 水印类型
     */
    private WatermarkType watermarkType;

    /**
     * 水印文件路径
     */
    private String watermarkPic;

    /**
     * 水印文字
     */
    private String watermarkText;

    /**
     * 水印透明度（取值范围0-1，0表示完全透明）
     */
    private float watermarkOpacity = 1.0f;

    /**
     * 水印位置
     */
    private Position watermarkPos = Positions.BOTTOM_RIGHT;

    /**
     * 校验参数
     * @return 校验通过
     */
    public boolean isValid() {
        return (!thumb || ((width > 0 && height > 0) || scale > 0)) && (!withWatermark || (watermarkType != null && watermarkPos != null
                && ((watermarkType == WatermarkType.PICTURE && StringUtils.isNotBlank(watermarkPic))
                || (watermarkType == WatermarkType.TEXT && StringUtils.isNotBlank(watermarkText)))));
    }
}
