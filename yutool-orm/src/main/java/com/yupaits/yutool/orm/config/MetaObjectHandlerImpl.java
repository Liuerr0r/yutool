package com.yupaits.yutool.orm.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.yupaits.yutool.orm.support.service.MetaObjectOptService;
import org.apache.ibatis.reflection.MetaObject;

import java.time.LocalDateTime;

/**
 * 元对象字段填充控制器实现，实现公共字段自动写入
 * @author yupaits
 * @date 2019/7/25
 */
public class MetaObjectHandlerImpl implements MetaObjectHandler {

    private final MetaObjectOptService metaObjectOptService;

    public MetaObjectHandlerImpl(MetaObjectOptService metaObjectOptService) {
        this.metaObjectOptService = metaObjectOptService;
    }

    @Override
    public void insertFill(MetaObject metaObject) {
        this.setInsertFieldValByName("createdTime", LocalDateTime.now(), metaObject);
        this.setInsertFieldValByName("createdBy", metaObjectOptService.getOperatorId(), metaObject);
        this.setInsertFieldValByName("lastModifiedTime", LocalDateTime.now(), metaObject);
        this.setInsertFieldValByName("lastModifiedBy", metaObjectOptService.getOperatorId(), metaObject);
        this.setInsertFieldValByName("deleted", false, metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.setUpdateFieldValByName("lastModifiedTime", LocalDateTime.now(), metaObject);
        this.setUpdateFieldValByName("lastModifiedBy", metaObjectOptService.getOperatorId(), metaObject);
    }
}
