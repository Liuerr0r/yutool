package com.yupaits.yutool.orm.base;

import com.yupaits.yutool.orm.support.ModelBuilder;
import com.yupaits.yutool.orm.support.VoBuilder;

/**
 * IBaseService接口
 * @author yupaits
 * @date 2019/8/7
 */
public interface IBaseService {
    /**
     * 设置默认的Vo类型Class和VoBuilder
     */
    void setDefaultVoConfig();

    /**
     * 设置默认的ModelBuilder
     */
    void setDefaultModelBuilder();

    /**
     * 设置VoClass，用于动态替换默认的VoClass类型
     * @param voClass voClass对象
     * @param <Vo> Vo类型
     */
    <Vo extends BaseVo> void setVoClass(Class<Vo> voClass);

    /**
     * 设置ModelBuilder，用于动态替换默认的ModelBuilder
     * @param modelBuilder modelBuilder对象
     */
    void setModelBuilder(ModelBuilder modelBuilder);

    /**
     * 设置VoBuilder，用于动态替换默认的VoBuilder
     * @param voBuilder voBuilder对象
     */
    void setVoBuilder(VoBuilder voBuilder);
}
