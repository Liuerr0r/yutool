package com.yupaits.yutool.cache.annotation;

import java.lang.annotation.*;

/**
 * 禁用缓存标识注解
 * @author yupaits
 * @date 2019/7/18
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DisableCache {
}
