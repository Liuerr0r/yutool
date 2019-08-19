package com.yupaits.yutool.push.support.webmsg;

/**
 * 全局触发动作枚举
 * @author yupaits
 * @date 2019/7/22
 */
public enum GlobalAction implements IAction {
    /**
     * 系统更新
     */
    SYSTEM_UPDATE,
    /**
     * 操作正常
     */
    NORMAL,
    /**
     * 心跳检查
     */
    HEARTBEAT,
    /**
     * 通知
     */
    NOTIFICATION,
    /**
     * 成功信息通知
     */
    SUCCESS_NOTIFY,
    /**
     * 错误信息通知
     */
    ERROR_NOTIFY,
    /**
     * 成功信息
     */
    SUCCESS_INFO,
    /**
     * 错误信息
     */
    ERROR_INFO,
    /**
     * 确认操作
     */
    CONFIRM,
    /**
     * 提示信息
     */
    NOTICE,
    /**
     * 交易成功
     */
    EXCHANGE_SUCCESS,
    /**
     * 重新登录
     */
    REDIRECT_LOGIN,
    /**
     * 会话超时
     */
    SESSION_TIMEOUT,
    /**
     * 出错
     */
    ERROR,
    /**
     * 锁屏
     */
    LOCK_SCREEN,
    /**
     * 系统定位
     */
    SYSTEM_LOCATE,
    /**
     * 清除数据
     */
    REMOVE_DATA,
    /**
     * 单据审核
     */
    ORDER_APPROVAL,
    /**
     * 审核抄送
     */
    APPROVAL_COPY,
    /**
     * 审核驳回
     */
    APPROVAL_REJECT;

    @Override
    public String getName() {
        return this.name();
    }
}
