package com.yupaits.yutool.orm.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.yupaits.yutool.commons.service.OptService;
import org.apache.ibatis.reflection.MetaObject;

import java.time.LocalDateTime;

/**
 * 元对象字段填充控制器实现，实现公共字段自动写入
 * @author yupaits
 * @date 2019/7/25
 */
public class MetaObjectHandlerImpl implements MetaObjectHandler {

    private final OptService optService;

    public MetaObjectHandlerImpl(OptService optService) {
        this.optService = optService;
    }

    @Override
    public void insertFill(MetaObject metaObject) {
        this.setInsertFieldValByName("createdTime", LocalDateTime.now(), metaObject);
        this.setInsertFieldValByName("createdBy", optService.getOperatorId(), metaObject);
        this.setInsertFieldValByName("lastModifiedTime", LocalDateTime.now(), metaObject);
        this.setInsertFieldValByName("lastModifiedBy", optService.getOperatorId(), metaObject);
        this.setInsertFieldValByName("deleted", false, metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.setUpdateFieldValByName("lastModifiedTime", LocalDateTime.now(), metaObject);
        this.setUpdateFieldValByName("lastModifiedBy", optService.getOperatorId(), metaObject);
    }
}
