package com.yupaits.yutool.orm.support;

import com.posun.cmpt.orm.base.BaseVo;

/**
 * ServiceContext
 * @author yupaits
 * @date 2019/8/13
 */
public class ServiceContext {

    /**
     * VoClass线程变量
     */
    private static final ThreadLocal<Class<? extends BaseVo>> VO_CLASS_CONTEXT = new ThreadLocal<>();

    /**
     * VoBuilder线程变量
     */
    private static final ThreadLocal<VoBuilder> VO_BUILDER_CONTEXT = new ThreadLocal<>();

    /**
     * ModelBuilder线程变量
     */
    private static final ThreadLocal<ModelBuilder> MODEL_BUILDER_CONTEXT = new ThreadLocal<>();

    /**
     * 设置VoClass
     * @param voClass VoClass参数
     */
    public static void setVoClass(Class<? extends BaseVo> voClass) {
        if (voClass != null) {
            VO_CLASS_CONTEXT.set(voClass);
        }
    }

    /**
     * 获取VoClass
     * @return VoClass参数
     */
    public static Class<? extends BaseVo> getVoClass() {
        return VO_CLASS_CONTEXT.get();
    }

    /**
     * 清除VoClass
     */
    public static void removeVoClass() {
        VO_CLASS_CONTEXT.remove();
    }

    /**
     * 设置VoBuilder
     * @param voBuilder VoBuilder对象
     */
    public static void setVoBuilder(VoBuilder voBuilder) {
        if (voBuilder != null) {
            VO_BUILDER_CONTEXT.set(voBuilder);
        }
    }

    /**
     * 获取VoBuilder
     * @return VoBuilder对象
     */
    public static VoBuilder getVoBuilder() {
        return VO_BUILDER_CONTEXT.get();
    }

    /**
     * 清除VoBuilder
     */
    public static void removeVoBuilder() {
        VO_BUILDER_CONTEXT.remove();
    }

    /**
     * 设置ModelBuilder
     * @param modelBuilder ModelBuilder对象
     */
    public static void setModelBuilder(ModelBuilder modelBuilder) {
        if (modelBuilder != null) {
            MODEL_BUILDER_CONTEXT.set(modelBuilder);
        }
    }

    /**
     * 获取ModelBuilder
     * @return ModelBuilder对象
     */
    public static ModelBuilder getModelBuilder() {
        return MODEL_BUILDER_CONTEXT.get();
    }

    /**
     * 清除ModelBuilder
     */
    public static void removeModelBuilder() {
        MODEL_BUILDER_CONTEXT.remove();
    }
}
