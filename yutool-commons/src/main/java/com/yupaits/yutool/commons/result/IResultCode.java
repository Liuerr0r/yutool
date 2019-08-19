package com.yupaits.yutool.commons.result;

/**
 * 响应码和响应语义枚举接口
 * @author yupaits
 * @date 2019/7/15
 */
public interface IResultCode {

    /**
     * 获取响应码
     * @return 响应码
     */
    int getCode();

    /**
     * 获取响应语义
     * @return 响应语义
     */
    String getMessage();
}
