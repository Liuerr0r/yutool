package com.yupaits.yutool.orm.annotation;

import java.lang.annotation.*;

/**
 * 审计字段标记注解
 * @author yupaits
 * @date 2019/7/25
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AuditLog {

    /**
     * 审计字段描述信息
     */
    String description() default "";
}
