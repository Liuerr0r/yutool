package com.yupaits.yutool.push.support.sms;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.crypto.SecureUtil;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * 电信短信服务
 * @author yupaits
 * @date 2019/7/26
 */
@Slf4j
public class CtSmsProvider implements SmsProvider {
    private static final String SMS_SERVER_URL = "http://client.sms10000.com/api/webservice";
    private static final String SMS_SEND_CMD = "send";
    private static final String SUCCESS_CODE = "1";

    private final CtInfoService ctInfoService;

    private RestTemplate restTemplate;

    @Value("${id.generator.workerId:0}")
    private long workerId;
    @Value("${id.generator.datacenterId:0}")
    private long datacenterId;

    public CtSmsProvider(CtInfoService ctInfoService) {
        this.ctInfoService = ctInfoService;
        this.restTemplate = new RestTemplate();
    }

    @Override
    public void sendSms(String phoneNumber, String content) {
        long timestamp = System.currentTimeMillis();
        String msgId = new Snowflake(workerId, datacenterId, true).nextIdStr();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Lists.newArrayList(MediaType.APPLICATION_JSON_UTF8));
        HttpEntity<Map> entity = new HttpEntity<>(headers);
        String url = SMS_SERVER_URL + "?" +
                "cmd=" + SMS_SEND_CMD + "&" +
                "eprId=" + ctInfoService.getEprId() + "&" +
                "userId=" + ctInfoService.getUserId() + "&" +
                "timestamp=" + timestamp + "&" +
                "key=" + generateKey(ctInfoService.getEprId(), ctInfoService.getUserId(),
                ctInfoService.getUserPassword(), timestamp) +
                "&" +
                "msgId=" + msgId + "&" +
                //1-json方式返回 2-xml方式返回
                "format=" + 1 + "&" +
                "mobile=" + phoneNumber + "&" +
                "content=" + new String(content.getBytes(StandardCharsets.UTF_8));
        ResponseEntity<String> resultRes = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        String resultStr = resultRes.getBody();
        CtSmsResult result = JSON.parseObject(resultStr, CtSmsResult.class);
        if (result == null || !StringUtils.equals(SUCCESS_CODE, result.getResult())) {
            log.error("使用电信短信服务发送短信失败，短信msgId：{}，返回结果：{}", msgId, result);
        } else {
            log.info("发送短信成功，短信msgId：{}", msgId);
        }
    }

    /**
     * 生成Key
     * @param eprId 企业ID
     * @param userId 用户ID
     * @param userPassword 用户密码
     * @param timestamp 时间戳
     * @return Key
     */
    private String generateKey(int eprId, String userId, String userPassword, long timestamp) {
        return SecureUtil.md5(eprId + userId + userPassword + timestamp);
    }

    /**
     * 发送短信结果
     */
    @Data
    public static class CtSmsResult implements Serializable {
        private static final long serialVersionUID = 1L;
        /**
         * 返回码
         */
        private String result;

        /**
         * 提示信息
         */
        private String tips;
    }
}
