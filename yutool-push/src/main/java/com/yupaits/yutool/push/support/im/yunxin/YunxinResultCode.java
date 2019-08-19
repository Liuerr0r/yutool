package com.yupaits.yutool.push.support.im.yunxin;

/**
 * 网易云信返回码枚举
 * @author yupaits
 * @date 2019/7/27
 */
public enum YunxinResultCode {
    /**
     * 操作成功
     */
    OK(200, "操作成功"),
    VERSION_ERROR(201, "客户端版本不对，需升级sdk"),
    DISABLED(301, "被封禁"),
    USERNAME_PASSWORD_ERROR(302, "用户名或密码错误"),
    IP_LIMIT(315, "IP限制"),
    FORBIDDEN(403, "非法操作或没有权限"),
    NOT_FOUND(404, "对象不存在"),
    PARAMS_TOO_LANG(405, "参数长度过程"),
    OBJECT_READ_ONLY(406, "对象只读"),
    REQUEST_TIMEOUT(408, "客户端请求超时"),
    VALIDATE_FAIL(413, "验证失败(短信服务)"),
    PARAMS_ERROR(414, "参数错误"),
    NETWORK_PROBLEM(415, "客户端网络问题"),
    RATE_LIMIT(416, "频率控制"),
    REPEAT_OPERATE(417, "重复操作"),
    CHANNEL_UNAVAILABLE(418, "通道不可用(短信服务)"),
    COUNT_UPPER_LIMIT(419, "数量超过上限"),
    ACCOUNT_DISABLED(422, "账号被禁用"),
    ACCOUNT_BANNED(423, "账号被禁言"),
    REPEAT_HTTP_REQUEST(431, "HTTP重复请求"),
    INTERNAL_SERVER_ERROR(500, "服务器内部错误"),
    SERVER_BUSY(503, "服务器繁忙"),
    MSG_REVOKE_TIMEOUT(508, "消息撤回超时"),
    INVALID_PROTOCOL(509, "无效协议"),
    SERVICE_NOT_AVAILABLE(514, "服务不可用"),
    UNPACKING_ERROR(998, "解包错误"),
    PACKAGING_ERROR(999, "打包错误"),
    //群相关错误码
    GROUP_PEOPLE_UPPER_LIMIT(801, "群人数达到上限"),
    NO_GROUP_PERMISSION(802, "没有权限"),
    GROUP_NOT_FOUND(803, "群不存在"),
    ACCOUNT_NOT_IN_GROUP(804, "用户不在群"),
    GROUP_TYPE_MISMATCH(805, "群类型不匹配"),
    GROUP_QUANTITY_UPPER_LIMIT(806, "创建群数量达到限制"),
    GROUP_MEMBER_STATUS_ERROR(807, "群成员状态错误"),
    APPLY_SUCCESS(808, "申请成功"),
    ALREADY_IN_GROUP(809, "已经在群内"),
    INVITE_SUCCESS(810, "邀请成功"),
    MENTION_ACCOUNTS_UPPER_LIMIT(811, "@账号数量超过限制"),
    GROUP_BANNED(812, "群禁言，普通成员不能发送消息"),
    GROUP_INVITE_PARTIAL_SUCCESS(813, "群拉人部分成功"),
    GROUP_HAVE_READ_DISABLED(814, "禁止使用群组已读服务"),
    GROUP_ADMINS_UPPER_LIMIT(815, "群管理员人数超过上限"),
    //音视频、白板通话相关错误码
    CHANNEL_INVALID(9102, "通道失效"),
    RESPONDED(9103, "已经在他端对这个呼叫响应过了"),
    TARGET_OFFLINE(11001, "通话不可达，对方离线状态"),
    //聊天室相关错误码
    IM_CONNECTION_EXCEPTION(13001, "IM主连接状态异常"),
    CHAT_ROOM_EXCEPTION(13002, "聊天室状态异常"),
    ACCOUNT_IN_BLACK_LIST(13003, "账号在黑名单中,不允许进入聊天室"),
    ACCOUNT_IN_BANNED_LIST(13004, "在禁言列表中,不允许发言"),
    ACCOUNT_ANTI_TRASH(13005, "用户的聊天室昵称、头像或成员扩展字段被反垃圾"),
    //特定业务相关错误码
    NOT_EMAIL(10431, "输入email不是邮箱"),
    NOT_PHONE(10432, "输入mobile不是手机号码"),
    PASSWORD_INCONSISTENT(10433, "注册输入的两次密码不相同"),
    ENTERPRISE_NOT_FOUND(10434, "企业不存在"),
    LOGIN_FAIL(10435, "登陆密码或帐号不对"),
    APP_NOT_FOUND(10436, "app不存在"),
    EMAIL_EXISTED(10437, "email已注册"),
    PHONE_EXISTED(10438, "手机号已注册"),
    APP_NAME_EXISTED(10441, "app名字已经存在");

    /**
     * 返回码
     */
    private int code;

    /**
     * 描述信息
     */
    private String msg;

    YunxinResultCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    /**
     * 根据code获取YunxinResultCode枚举项
     * @param code code
     * @return 枚举项
     */
    public static YunxinResultCode valueOf(int code) {
        YunxinResultCode[] codes = YunxinResultCode.values();
        for (YunxinResultCode resultCode : codes) {
            if (resultCode.code == code) {
                return resultCode;
            }
        }
        return null;
    }
}
