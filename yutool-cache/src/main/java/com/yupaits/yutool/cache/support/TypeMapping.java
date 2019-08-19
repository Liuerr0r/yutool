package com.yupaits.yutool.cache.support;

import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

/**
 * Java数据类型与Redis数据类型映射关系，用于自动匹配
 * @author yupaits
 * @date 2019/7/17
 */
public class TypeMapping {

    public static Map<Class<?>, CacheValueType> mapping = Maps.newHashMap();

    static {
        mapping.put(List.class, CacheValueType.LIST);
        mapping.put(Set.class, CacheValueType.SET);
        mapping.put(SortedSet.class, CacheValueType.ZSET);
        mapping.put(Map.class, CacheValueType.HASH);
    }

    public static CacheValueType getValueType(Class<?> clazz) {
        Set<Class<?>> keys = mapping.keySet();
        for (Class<?> keyClass : keys) {
            if (keyClass.isAssignableFrom(clazz)) {
                return mapping.get(keyClass);
            }
        }
        return CacheValueType.VALUE;
    }
}
