package com.yupaits.yutool.push.support.im.yunxin;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.yupaits.yutool.push.exception.PushException;
import com.yupaits.yutool.push.model.msg.ImMsg;
import com.yupaits.yutool.push.support.im.ImMsgType;
import com.yupaits.yutool.push.support.im.ImProvider;
import com.yupaits.yutool.push.utils.CheckSumBuilder;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * 网易云信IM消息发送
 * @author yupaits
 * @date 2019/7/27
 */
@Slf4j
@Accessors(chain = true)
public class YunxinProvider implements ImProvider {
    private static final String SEND_MSG_URL = "https://api.netease.im/nimserver/msg/sendMsg.action";
    private static final String CREATE_ACCID_URL = "https://api.netease.im/nimserver/user/create.action";
    private static final String CONTENT_TYPE_SUFFIX = ";charset=utf-8";
    private static final String APP_KEY_HEADER = "AppKey";
    private static final String NONCE_HEADER = "Nonce";
    private static final String CUR_TIME_HEADER = "CurTime";
    private static final String CHECK_SUM_HEADER = "CheckSum";

    private final RestTemplate restTemplate;

    @Value("${netease.yunxin.appkey:}")
    private String appKey;
    @Value("${netease.yunxin.appSecret:}")
    private String appSecret;

    public YunxinProvider() {
        this.restTemplate = new RestTemplate();
    }

    @Override
    public void sendImMsg(ImMsg imMsg, String to, String content) {
        YunxinMsg msg = YunxinMsg.builder()
                .from(imMsg.getFrom())
                .ope(imMsg.getSendType().ordinal())
                .to(to)
                .type(imMsg.getType().getCode())
                .antispam(MapUtils.getString(imMsg.getExtras(), "antispam"))
                .antispamCustom(MapUtils.getString(imMsg.getExtras(), "antispamCustom"))
                .pushContent(MapUtils.getString(imMsg.getExtras(), "pushContent"))
                .payload(MapUtils.getString(imMsg.getExtras(), "payload"))
                .ext(MapUtils.getString(imMsg.getExtras(), "ext"))
                .forcepushlist(MapUtils.getString(imMsg.getExtras(), "forcepushlist"))
                .forcepushcontent(MapUtils.getString(imMsg.getExtras(), "forcepushcontent"))
                .forcepushall(MapUtils.getString(imMsg.getExtras(), "forcepushall"))
                .bid(MapUtils.getString(imMsg.getExtras(), "bid"))
                .useYidun(MapUtils.getInteger(imMsg.getExtras(), "useYidun"))
                .markRead(MapUtils.getInteger(imMsg.getExtras(), "markRead"))
                .checkFriend(MapUtils.getBooleanValue(imMsg.getExtras(), "checkFriend"))
                .build();
        Object optionObj = MapUtils.getObject(imMsg.getExtras(), "option");
        if (optionObj instanceof YunxinMsg.Option) {
            msg.setOption((YunxinMsg.Option) optionObj);
        }
        buildMsgBody(msg, content);
        MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
        //YunxinMsg转成RestTemplate支持的MultiValueMap
        //noinspection unchecked
        Map<String, Object> msgMap = JSON.parseObject(JSON.toJSONString(msg), Map.class);
        if (MapUtils.isNotEmpty(msgMap)) {
            msgMap.forEach(params::add);
        }
        HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(params, getHeaders());
        ResponseEntity<YunxinResult> response = restTemplate.exchange(SEND_MSG_URL, HttpMethod.POST, entity, YunxinResult.class);
        YunxinResult result = response.getBody();
        if (result != null) {
            YunxinResultCode resultCode = YunxinResultCode.valueOf(result.getCode());
            if (resultCode == null) {
                log.error("发送IM消息失败，无法识别的返回码：{}，返回内容：{}", result.getCode(), result);
                return;
            }
            if (resultCode == YunxinResultCode.OK) {
                log.info("发送IM消息成功，返回内容：{}", result.getData());
            } else {
                log.error("发送IM消息返回码：{}，返回信息：{}，详情：{}", result.getCode(), resultCode.getMsg(), result.getDesc());
            }
        }
    }

    /**
     * 创建网易云通信ID
     */
    public void createAccId(AccIdCreate accIdCreate) throws PushException {
        if (!accIdCreate.isValid()) {
            throw new PushException("创建网易云通信ID参数校验失败，accid不能为空");
        }
        MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
        //AccIdCreate转成MultiValueMap
        //noinspection unchecked
        Map<String, Object> map = JSON.parseObject(JSON.toJSONString(accIdCreate), Map.class);
        if (MapUtils.isNotEmpty(map)) {
            map.forEach(params::add);
        }
        HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(params, getHeaders());
        ResponseEntity<YunxinResult> response = restTemplate.exchange(CREATE_ACCID_URL, HttpMethod.POST, entity, YunxinResult.class);
        YunxinResult result = response.getBody();
        if (result != null) {
            YunxinResultCode resultCode = YunxinResultCode.valueOf(result.getCode());
            if (resultCode == null) {
                log.error("创建网易云通信ID失败，无法识别的返回码：{}，返回内容：{}", result.getCode(), result);
                return;
            }
            if (resultCode == YunxinResultCode.OK) {
                log.info("创建网易云通信ID成功，云通信ID：{}", result.getInfo());
            } else {
                log.error("创建网易云通信ID返回码：{}，返回信息：{}，详情：{}", result.getCode(), resultCode.getMsg(), result.getDesc());
            }
        }
    }

    /**
     * 构建消息Body
     */
    private void buildMsgBody(YunxinMsg msg, String content) {
        //根据模板发送消息暂只支持文本消息
        YunxinMsg.Body body = new YunxinMsg.Body().setMsg(content);
        msg.setType(ImMsgType.TEXT.getCode());
        msg.setBody(body);
    }

    /**
     * 构造并获取HttpHeaders
     */
    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.put(HttpHeaders.CONTENT_TYPE, Lists.newArrayList(MediaType.APPLICATION_FORM_URLENCODED_VALUE + CONTENT_TYPE_SUFFIX));
        headers.setAccept(Lists.newArrayList(MediaType.APPLICATION_JSON_UTF8));
        headers.put(APP_KEY_HEADER, Lists.newArrayList(appKey));
        String nonce = RandomStringUtils.randomAlphanumeric(16);
        headers.put(NONCE_HEADER, Lists.newArrayList(nonce));
        String curTime = String.valueOf(System.currentTimeMillis() / 1000);
        headers.put(CUR_TIME_HEADER, Lists.newArrayList(curTime));
        headers.put(CHECK_SUM_HEADER, Lists.newArrayList(CheckSumBuilder.getCheckSum(appSecret, nonce, curTime)));
        return headers;
    }
}
