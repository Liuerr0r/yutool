package com.yupaits.yutool.commons.result;

/**
 * 响应结果生成
 * @author yupaits
 * @date 2019/7/15
 */
public class ResultWrapper {
    public static Result success() {
        return Result.builder()
                .code(ResultCode.OK.getCode())
                .message(ResultCode.OK.getMessage())
                .build();
    }

    public static <T> Result<T> success(T data) {
        return Result.<T>builder()
                .code(ResultCode.OK.getCode())
                .message(ResultCode.OK.getMessage())
                .data(data)
                .build();
    }

    public static Result fail(IResultCode resultCode) {
        return Result.builder()
                .code(resultCode.getCode())
                .message(resultCode.getMessage())
                .build();
    }

    public static Result fail(String msg) {
        return Result.builder()
                .code(ResultCode.FAIL.getCode())
                .message(msg)
                .build();
    }

    public static Result fail(int code, String msg) {
        return Result.builder()
                .code(code)
                .message(msg)
                .build();
    }
}
