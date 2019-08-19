package com.yupaits.yutool.orm.support;

import lombok.Builder;
import lombok.Data;

/**
 * Vo类返回携带信息配置
 * @author yupaits
 * @date 2019/7/15
 */
@Data
@Builder
public class VoProps {
    /**
     * 是否携带CreatedAt
     */
    private boolean withCreatedTime;

    /**
     * 是否携带CreatedBy
     */
    private boolean withCreatedBy;

    /**
     * 是否携带CreatedName
     */
    private boolean withCreatedByName;

    /**
     * 是否携带UpdatedAt
     */
    private boolean withLastModifiedTime;

    /**
     * 是否携带UpdatedBy
     */
    private boolean withLastModifiedBy;

    /**
     * 是否携带updatedName
     */
    private boolean withLastModifiedByName;

    /**
     * 默认配置
     */
    public static VoProps defaultProps() {
        return withAll();
    }

    /**
     * 携带列出的所有信息
     */
    public static VoProps withAll() {
        return VoProps.builder()
                .withCreatedTime(true)
                .withCreatedBy(true)
                .withCreatedByName(true)
                .withLastModifiedTime(true)
                .withLastModifiedBy(true)
                .withLastModifiedByName(true)
                .build();
    }

    /**
     * 不携带列出的信息
     */
    public static VoProps withoutAll() {
        return VoProps.builder()
                .withCreatedTime(false)
                .withCreatedBy(false)
                .withCreatedByName(false)
                .withLastModifiedTime(false)
                .withLastModifiedBy(false)
                .withLastModifiedByName(false)
                .build();
    }

    /**
     * 按需携带
     * @param optTime 携带操作时间
     * @param optBy 携带操作人ID
     * @param optName 携带操作人用户名
     */
    public static VoProps with(boolean optTime, boolean optBy, boolean optName) {
        return VoProps.builder()
                .withCreatedTime(optTime)
                .withCreatedBy(optBy)
                .withCreatedByName(optName)
                .withLastModifiedTime(optTime)
                .withLastModifiedBy(optBy)
                .withLastModifiedByName(optName)
                .build();
    }

    /**
     * 按需携带
     * @param created 携带记录创建信息
     * @param lastModified 携带记录更新信息
     */
    public static VoProps with(boolean created, boolean lastModified) {
        return VoProps.builder()
                .withCreatedTime(created)
                .withCreatedBy(created)
                .withCreatedByName(created)
                .withLastModifiedTime(lastModified)
                .withLastModifiedBy(lastModified)
                .withLastModifiedByName(lastModified)
                .build();
    }
}
