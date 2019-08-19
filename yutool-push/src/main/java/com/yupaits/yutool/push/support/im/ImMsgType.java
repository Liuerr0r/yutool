package com.yupaits.yutool.push.support.im;

/**
 * IM消息类型
 * @author yupaits
 * @date 2019/7/27
 */
public enum ImMsgType {
    /**
     * 文本
     */
    TEXT(0),
    /**
     * 图片
     */
    PICTURE(1),
    /**
     * 语音
     */
    VOICE(2),
    /**
     * 视频
     */
    VIDEO(3),
    /**
     * 地理位置信息
     */
    LOCATION(4),
    /**
     * 文件
     */
    FILE(6),
    /**
     * 自定义消息类型
     */
    CUSTOM(100);

    /**
     * IM消息类型编码
     */
    private int code;

    ImMsgType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
