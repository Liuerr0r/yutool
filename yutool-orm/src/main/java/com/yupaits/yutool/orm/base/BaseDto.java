package com.yupaits.yutool.orm.base;

import org.apache.commons.collections4.CollectionUtils;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Comparator;

/**
 * Dto基类
 * @author yupaits
 * @date 2019/7/15
 */
public abstract class BaseDto<ID extends Serializable> implements Serializable {

    /**
     * Dto校验方法
     * @return 校验结果
     */
    public abstract boolean isValid();

    /**
     * 逻辑唯一字段，用于更新数据时进行校验
     * @return 逻辑唯一字段数组
     */
    public String[] uniqueFields() {
        return new String[]{};
    }

    /**
     * 组合唯一索引，用于更新数据时进行校验
     * @return 组合唯一索引
     */
    public String[] unionKeyFields() {
        return new String[]{};
    }

    /**
     * 默认的Dto排序实现
     * @param <D> Dto类型
     * @return Comparator实现
     */
    @SuppressWarnings("unchecked")
    public <D extends BaseDto> Comparator<D> comparator() {
        return (o1, o2) -> {
            ID id1 = (ID) o1.fetchId();
            ID id2 = (ID) o2.fetchId();
            if (id1 instanceof String && id2 instanceof String) {
                return ((String) id1).compareTo((String) id2);
            }
            if (id1 instanceof Long && id2 instanceof Long) {
                return ((Long) id1).compareTo((Long) id2);
            }
            return 0;
        };
    }

    /**
     * 获取ID
     * @return ID
     */
    @SuppressWarnings("unchecked")
    public ID fetchId() {
        ID id = null;
        Field idField = null;
        boolean accessible = false;
        try {
            idField = this.getClass().getDeclaredField("id");
            accessible = idField.isAccessible();
            idField.setAccessible(true);
            id = (ID) idField.get(this);
        } catch (NoSuchFieldException | IllegalAccessException ignored) {
        } finally {
            if (idField != null) {
                idField.setAccessible(accessible);
            }
        }
        return id;
    }

    /**
     * Dto集合校验方法，默认不允许集合为空
     * @param collection Dto集合
     * @return 校验结果
     */
    public static <D extends BaseDto> boolean isValid(Collection<D> collection) {
        return isValid(collection, false);
    }

    /**
     * Dto集合校验方法
     * @param collection Dto集合
     * @param allowEmpty 是否允许集合为空
     * @return 校验结果
     */
    public static <D extends BaseDto> boolean isValid(Collection<D> collection, boolean allowEmpty) {
        if (!allowEmpty && CollectionUtils.isEmpty(collection)) {
            return false;
        }
        for (D dto : collection) {
            if (!dto.isValid()) {
                return false;
            }
        }
        return true;
    }
}
