package com.yupaits.yutool.commons.utils;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.regex.Pattern;

/**
 * 校验工具
 * @author yupaits
 * @date 2019/7/15
 */
public class ValidateUtils {

    /**
     * 检查ID是否有效
     * @param id id
     * @param <T> ID类型
     * @return 校验结果
     */
    public static <T extends Serializable> boolean idValid(T id) {
        if (id instanceof Long) {
            return idValid((Long) id);
        }
        if (id instanceof String) {
            return idValid((String) id);
        }
        if (id instanceof Integer) {
            return idValid((Integer) id);
        }
        return false;
    }

    /**
     * 检查ID是否有效
     * @param id id
     * @return 校验结果
     */
    public static boolean idValid(Long id) {
        return id != null && id.compareTo(0L) > 0;
    }

    /**
     * 检查ID是否有效
     * @param id id
     * @return 校验结果
     */
    public static boolean idValid(Integer id) {
        return id != null && id.compareTo(0) > 0;
    }

    /**
     * 检查ID是否有效
     * @param id id
     * @return 校验结果
     */
    public static boolean idValid(String id) {
        return StringUtils.isNotBlank(id);
    }

    /**
     * 判断是否为正整数
     * @param number 待判断的数
     * @return 校验结果
     */
    public static boolean isPositive(Integer number) {
        return number != null && number.compareTo(0) > 0;
    }

    /**
     * 判断是否为正浮点数
     * @param number 带判断的数
     * @return 校验结果
     */
    public static boolean isPositive(Float number) {
        return number != null && number.compareTo(0f) > 0;
    }

    /**
     * 判断是否为正小数
     * @param number 待判断的数
     * @return 校验结果
     */
    public static boolean isPositive(BigDecimal number) {
        return number != null && number.compareTo(BigDecimal.ZERO) > 0;
    }

    /**
     * 判断是否为负整数
     * @param number 待判断的数
     * @return 校验结果
     */
    public static boolean isNegative(Integer number) {
        return number != null && number.compareTo(0) < 0;
    }

    /**
     * 判断是否为负小数
     * @param number 待判断的数
     * @return 校验结果
     */
    public static boolean isNegative(BigDecimal number) {
        return number != null && number.compareTo(BigDecimal.ZERO) < 0;
    }

    /**
     * 判断是否为非负整数
     * @param number 待判断的数
     * @return 校验结果
     */
    public static boolean isNotNegative(Integer number) {
        return number != null && number.compareTo(0) >= 0;
    }

    /**
     * 判断是否为非负小数
     * @param number 待判断的数
     * @return 校验结果
     */
    public static boolean isNotNegative(BigDecimal number) {
        return number != null && number.compareTo(BigDecimal.ZERO) >= 0;
    }

    /**
     * 检查手机号码是否有效
     * @param phoneNumber 手机号码
     * @return 校验结果
     */
    public static boolean isPhoneNumber(String phoneNumber) {
        if (StringUtils.isBlank(phoneNumber)) {
            return false;
        }
        String regex = "^1([358][0-9]|4[579]|66|7[0135678]|9[89])[0-9]{8}$";
        return Pattern.compile(regex).matcher(phoneNumber).matches();
    }

    /**
     * 检查IP地址是否有效
     * @param ip IP地址
     * @return 校验结果
     */
    public static boolean isIP(String ip) {
        if (StringUtils.isBlank(ip)) {
            return false;
        }
        String regex = "^((?:(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))\\.){3}(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d))))$";
        return Pattern.compile(regex).matcher(ip).matches();
    }

    /**
     * 检查Email是否有效
     * @param email Email
     * @return 校验结果
     */
    public static boolean isEmail(String email) {
        if (StringUtils.isBlank(email)) {
            return false;
        }
        String regex = "^[a-zA-Z0-9_.-]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*\\.[a-zA-Z0-9]{2,6}$";
        return Pattern.compile(regex).matcher(email).matches();
    }

    /**
     * 判断Boolean对象是否为真
     * @param boolObj Boolean对象
     * @return 判单结果
     */
    public static boolean isTrue(Boolean boolObj) {
        return boolObj != null && boolObj;
    }
}
