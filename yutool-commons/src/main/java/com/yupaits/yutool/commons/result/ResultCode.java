package com.yupaits.yutool.commons.result;

/**
 * 响应码和响应语义枚举
 * @author yupaits
 * @date 2019/7/15
 */
public enum ResultCode implements IResultCode {
    /**
     * 处理成功
     */
    OK(200, "成功"),
    UNAUTHORIZED(401,"访问被拒绝，未经授权的访问"),
    FORBIDDEN(403, "无访问权限"),
    INTERNAL_SERVER_ERROR(500, "内部服务错误"),
    /**
     * 默认的失败响应码
     */
    FAIL(10000, "业务异常"),
    LOGIN_FAIL(10001, "用户名或密码错误"),
    PARAMS_ERROR(11001, "参数校验失败"),
    DATA_NOT_FOUND(11002, "查找的数据不存在或已被删除"),
    DATA_CANNOT_DELETE(11003, "数据无法删除"),
    DATA_CONFLICT(11004, "不允许的数据重复"),
    CREATE_FAIL(20001, "创建记录失败"),
    UPDATE_FAIL(20002, "更新记录失败"),
    DELETE_FAIL(20003, "删除记录失败"),
    SAVE_FAIL(20004, "保存记录失败"),
    FILE_UPLOAD_FAIL(30001, "文件上传失败");

    private int code;
    private String message;

    ResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public static ResultCode valueOf(int code) {
        for (ResultCode resultCode : ResultCode.values()) {
            if (resultCode.getCode() == code) {
                return resultCode;
            }
        }
        return null;
    }
}
