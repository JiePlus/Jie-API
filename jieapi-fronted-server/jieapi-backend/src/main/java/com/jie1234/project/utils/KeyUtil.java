package com.jie1234.project.utils;

import cn.hutool.captcha.generator.RandomGenerator;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.digest.DigestUtil;

/**
 * accessKey和secretKey工具类
 *
 * @author Jie
 */
public class KeyUtil {

    private final static String SALT = "jie";

    private final static String SPACE = "-";

    /**
     * 生成accessKey
     *
     * @param userAccount 用户账号
     * @return accessKey
     */
    public static String getAccessKey(String userAccount) {

        String randomNum = RandomUtil.randomNumbers(5);
        String str = SALT + SPACE + userAccount + SPACE + randomNum;

        return DigestUtil.md5Hex(str);

    }

    /**
     * 生成secretKey
     *
     * @param accessKey accessKey
     * @return secretKey
     */
    public static String getSecretKey(String accessKey) {

        return DigestUtil.md5Hex(accessKey);

    }

    /**
     * key值脱敏
     *
     * @param keyValue key值
     * @return 脱敏后的key值
     */
    public static String keyDesensitization(String keyValue) {
        if (keyValue == null) {
            return null;
        }
        // 只保留前后两位 中间用*代替
        return keyValue.substring(0, 2) + "******" + keyValue.substring(keyValue.length() - 2);
    }


}
