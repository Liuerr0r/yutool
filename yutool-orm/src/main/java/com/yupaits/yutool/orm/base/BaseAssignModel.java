package com.yupaits.yutool.orm.base;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author yupaits
 * @date 2019/7/15
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BaseAssignModel<ID extends Serializable, T extends Model<T>> extends AbstractModel<ID, T> {

    @TableId(value = "id", type = IdType.INPUT)
    private ID id;
}
