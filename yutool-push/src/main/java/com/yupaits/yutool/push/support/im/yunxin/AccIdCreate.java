package com.yupaits.yutool.push.support.im.yunxin;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/**
 * 网易云通信ID创建对象
 * 详见文档：https://dev.yunxin.163.com/docs/product/IM%E5%8D%B3%E6%97%B6%E9%80%9A%E8%AE%AF/%E6%9C%8D%E5%8A%A1%E7%AB%AFAPI%E6%96%87%E6%A1%A3/%E7%BD%91%E6%98%93%E4%BA%91%E9%80%9A%E4%BF%A1ID
 * @author yupaits
 * @date 2019/8/13
 */
@Data
@ApiModel(description = "网易云通信ID创建对象")
public class AccIdCreate implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 网易云通信ID，最大长度32字符，必须保证一个APP内唯一（只允许字母、数字、半角下划线_、@、半角点以及半角-组成，不区分大小写，会统一小写处理，请注意以此接口返回结果中的accid为准）
     */
    @ApiModelProperty(value = "网易云通信ID", required = true)
    private String accid;

    /**
     * 网易云通信ID昵称，最大长度64字符，用来PUSH推送
     * 时显示的昵称
     */
    @ApiModelProperty(value = "昵称")
    private String name;

    /**
     * json属性，第三方可选填，最大长度1024字符
     */
    @ApiModelProperty(value = "json属性，第三方可选填")
    private String props;

    /**
     * 网易云通信ID头像URL，第三方可选填，最大长度1024
     */
    @ApiModelProperty(value = "网易云通信ID头像URL")
    private String icon;

    /**
     * 网易云通信ID可以指定登录token值，最大长度128字符，并更新，如果未指定，会自动生成token，并在创建成功后返回
     */
    @ApiModelProperty(value = "登录token")
    private String token;

    /**
     * 用户签名，最大长度256字符
     */
    @ApiModelProperty(value = "用户签名")
    private String sign;

    /**
     * 用户email，最大长度64字符
     */
    @ApiModelProperty(value = "用户email")
    private String email;

    /**
     * 用户生日，最大长度16字符
     */
    @ApiModelProperty(value = "用户生日")
    private String birth;

    /**
     * 用户mobile，最大长度32字符，非中国大陆手机号码需要填写国家代码(如美国：+1-xxxxxxxxxx)或地区代码(如香港：+852-xxxxxxxx)
     */
    @ApiModelProperty(value = "用户mobile")
    private String mobile;

    /**
     * 用户性别，0表示未知，1表示男，2女表示女，其它会报参数错误
     */
    @ApiModelProperty(value = "用户性别 0表示未知，1表示男，2女表示女", allowableValues = "0, 1, 2")
    private int gender;

    /**
     * 用户名片扩展字段，最大长度1024字符，用户可自行扩展，建议封装成JSON字符串
     */
    @ApiModelProperty(value = "用户名片扩展字段")
    private String ex;

    /**
     * 参数校验
     * @return 校验通过
     */
    public boolean isValid() {
        return StringUtils.isNotBlank(accid);
    }
}
