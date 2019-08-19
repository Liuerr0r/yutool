package com.yupaits.yutool.cache.annotation;

import java.lang.annotation.*;

/**
 * 缓存清除标识注解
 * @author yupaits
 * @date 2019/7/18
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EvictCache {
}
