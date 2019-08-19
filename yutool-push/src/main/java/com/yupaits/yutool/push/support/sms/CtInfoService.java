package com.yupaits.yutool.push.support.sms;

/**
 * 电信短信读取企业用户参数服务接口
 * @author yupaits
 * @date 2019/8/12
 */
public interface CtInfoService {

    /**
     * 获取企业Id
     * @return 企业Id
     */
    int getEprId();

    /**
     * 获取用户Id
     * @return 用户Id
     */
    String getUserId();

    /**
     * 获取用户密码
     * @return 用户密码
     */
    String getUserPassword();
}
