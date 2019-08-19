package com.yupaits.yutool.orm.support;

import com.yupaits.yutool.orm.base.AbstractModel;
import com.yupaits.yutool.orm.base.BaseVo;

/**
 * @author yupaits
 * @date 2019/7/15
 */
public abstract class VoBuilder<Vo extends BaseVo, M extends AbstractModel> {

    /**
     * 构建Vo
     * @param vo vo对象
     * @param model model对象
     */
    public abstract void buildVo(Vo vo, M model);
}
