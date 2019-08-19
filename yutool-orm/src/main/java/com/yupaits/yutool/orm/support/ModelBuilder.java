package com.yupaits.yutool.orm.support;

import com.posun.cmpt.orm.base.AbstractModel;
import com.posun.cmpt.orm.base.BaseDto;

/**
 * @author yupaits
 * @date 2019/7/15
 */
public abstract class ModelBuilder<M extends AbstractModel, Dto extends BaseDto> {

    /**
     * 构建Model
     * @param model model对象
     * @param dto dto对象
     */
    public abstract void buildModel(M model, Dto dto);
}
