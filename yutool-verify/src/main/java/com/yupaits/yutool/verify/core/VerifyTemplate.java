package com.yupaits.yutool.verify.core;

import com.yupaits.yutool.cache.core.CacheTemplate;
import com.yupaits.yutool.cache.exception.CacheException;
import com.yupaits.yutool.cache.support.CacheKeyStrategy;
import com.yupaits.yutool.cache.support.CacheKeyType;
import com.yupaits.yutool.cache.support.CacheProps;
import com.yupaits.yutool.cache.support.CacheValueType;
import com.yupaits.yutool.push.core.PushTemplate;
import com.yupaits.yutool.push.exception.PushException;
import com.yupaits.yutool.push.model.msg.SmsMsg;
import com.yupaits.yutool.push.support.PushProps;
import com.yupaits.yutool.push.support.PushType;
import com.yupaits.yutool.verify.config.CodeSmsProps;
import com.yupaits.yutool.verify.support.CodeSmsKey;
import com.yupaits.yutool.verify.support.CodeSmsParams;
import com.yupaits.yutool.verify.support.ISmsScene;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.concurrent.TimeUnit;

/**
 * 验证工具类
 * @author yupaits
 * @date 2019/8/19
 */
public class VerifyTemplate {
    private final PushTemplate pushTemplate;
    private final CacheTemplate cacheTemplate;
    private final CodeSmsProps codeSmsProps;

    public VerifyTemplate(PushTemplate pushTemplate, CacheTemplate cacheTemplate, CodeSmsProps codeSmsProps) {
        this.pushTemplate = pushTemplate;
        this.cacheTemplate = cacheTemplate;
        this.codeSmsProps = codeSmsProps;
    }

    /**
     * 发送包含验证码的短信
     * @param smsScene 短信场景
     * @param mobile 目标手机号
     */
    public void sendCodeSms(ISmsScene smsScene, String mobile, CodeSmsParams params) throws PushException, CacheException {
        String code = RandomStringUtils.randomNumeric(codeSmsProps.getDigits());
        if (params == null) {
            params = new CodeSmsParams();
        }
        params.setCode(code);
        SmsMsg smsMsg = new SmsMsg().setMsgTemplate(smsScene.getSmsTemplate()).setParams(params);
        MultiValueMap<PushType, String> receivers = new LinkedMultiValueMap<>();
        receivers.add(PushType.SMS, mobile);
        PushProps pushProps = PushProps.builder()
                .receivers(receivers)
                .build();
        pushTemplate.push(smsMsg, pushProps);
        String codeSmsKey = new CodeSmsKey(smsScene, mobile).cacheKey();
        cacheTemplate.setCache(codeSmsKey, code, smsCodeCacheProps());
    }

    /**
     * 短信验证码校验
     * @param smsScene 短信场景
     * @param mobile 手机号
     * @param inputCode 输入的验证码
     * @return 校验结果
     */
    public boolean verifySmsCode(ISmsScene smsScene, String mobile, String inputCode) throws CacheException {
        String codeSmsKey = new CodeSmsKey(smsScene, mobile).cacheKey();
        String code = cacheTemplate.getCache(codeSmsKey, smsCodeCacheProps());
        return StringUtils.equals(inputCode, code);
    }

    /**
     * 获取短信验证码缓存配置
     * @return 缓存配置
     */
    private CacheProps smsCodeCacheProps() {
        return CacheProps.builder()
                .cacheLocal(false)
                .cacheDistribute(true)
                .expired(true)
                .timeout(codeSmsProps.getTimeout())
                .timeUnit(TimeUnit.MINUTES)
                .keyType(CacheKeyType.STRING)
                .keyStrategy(CacheKeyStrategy.TO_STRING)
                .valueType(CacheValueType.VALUE)
                .build();
    }
}
