package com.yupaits.yutool.push.support.im.yunxin;

import lombok.Data;

import java.io.Serializable;

/**
 * 网易云信Http响应内容
 * @author yupaits
 * @date 2019/7/27
 */
@Data
public class YunxinResult implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 返回码
     */
    private int code;

    /**
     * 返回描述
     */
    private String desc;

    /**
     * 返回内容
     */
    private ResultData data;

    /**
     * 返回信息
     */
    private ResultInfo info;

    @Data
    public static class ResultData implements Serializable {
        private static final long serialVersionUID = 1L;

        /**
         * 消息ID
         */
        private long msgid;

        /**
         * 消息发送的时间戳
         */
        private long timetag;

        /**
         * 是否使用易盾检测消息
         */
        private boolean antispam;
    }

    @Data
    public static class ResultInfo implements Serializable {
        private static final long serialVersionUID = 1L;

        /**
         * AccId
         */
        private String accid;

        /**
         * Token
         */
        private String token;

        /**
         * 昵称
         */
        private String name;
    }
}
