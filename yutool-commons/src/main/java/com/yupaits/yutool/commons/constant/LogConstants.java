package com.yupaits.yutool.commons.constant;

/**
 * 日志常量
 * @author yupaits
 * @date 2019/7/15
 */
public class LogConstants {
    public static final String SAVE_SUCCESS = "保存成功";
    public static final String DELETE_SUCCESS = "删除成功";
    public static final String VALIDATE_FAIL = "校验失败";
    public static final String VALIDATE_OR_CONFLICT_FAIL = "数据重复或者" + VALIDATE_FAIL;
    public static final String EXCEPTION_INFO = "应用程序异常";
    public static final String SAVE_SUCCESS_WITH_PARAM = SAVE_SUCCESS + ", 参数: {}";
    public static final String DELETE_SUCCESS_WITH_PARAM = DELETE_SUCCESS + ", 参数: {}";
    public static final String VALIDATE_FAIL_WITH_PARAM = VALIDATE_FAIL + ", 参数: {}";
    public static final String VALIDATE_OR_CONFLICT_FAIL_WITH_PARAM = VALIDATE_OR_CONFLICT_FAIL + ", 参数: {}";
    public static final String EXCEPTION_INFO_WITH_CODE = EXCEPTION_INFO + ", 异常码: {}";
    public static final String EXCEPTION_INFO_WITH_CODE_OPERATOR = EXCEPTION_INFO_WITH_CODE + ", 操作人: {}";
}
