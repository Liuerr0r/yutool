package com.yupaits.yutool.plugin.auditlogger.support;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.Data;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

/**
 * 审计元数据信息
 * @author yupaits
 * @date 2019/7/25
 */
@Data
public class AuditMetaInfo {

    /**
     * 数据表名
     */
    private String tableName;

    /**
     * 需要审计的属性
     */
    private List<Field> auditFields = Lists.newArrayList();

    /**
     * 属性描述信息
     */
    private Map<String, String> fieldDescriptionMap = Maps.newHashMap();
}
