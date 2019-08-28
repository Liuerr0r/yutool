package com.yupaits.yutool.verify.support;

import com.yupaits.yutool.cache.support.CacheKeyGenerator;

/**
 * 验证码短信缓存Key
 * @author yupaits
 * @date 2019/8/27
 */
public class CodeSmsKey implements CacheKeyGenerator {
    private static final String CODE_SMS_KEY_PREFIX = "sms:code:";
    private static final String DELIMITER = ":";

    private final ISmsScene smsScene;
    private final String mobile;

    public CodeSmsKey(ISmsScene smsScene, String mobile) {
        this.smsScene = smsScene;
        this.mobile = mobile;
    }

    @Override
    public String cacheKey() {
        return CODE_SMS_KEY_PREFIX + mobile + DELIMITER + smsScene.getSceneCode();
    }
}
