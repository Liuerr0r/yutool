package com.yupaits.yutool.orm.base;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author yupaits
 * @date 2019/7/15
 */
@Data
@EqualsAndHashCode(callSuper = false)
public abstract class AbstractModel<ID extends Serializable, T extends Model<T>> extends Model<T> {

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdTime;

    /**
     * 创建人ID
     */
    @TableField(fill = FieldFill.INSERT)
    private String createdBy;

    /**
     * 最近更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime lastModifiedTime;

    /**
     * 最近更新人ID
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String lastModifiedBy;

    /**
     * 获取ID
     * @return ID
     */
    public abstract ID getId();

    /**
     * 设置ID
     * @param id ID
     */
    public abstract void setId(ID id);
}
