package com.turbolisten.bot.service.util;

import com.turbolisten.bot.service.common.constant.CommonConst;
import org.apache.commons.codec.digest.DigestUtils;

public class SmartDigestUtil extends DigestUtils {

    /**
     * md5 使用默认加盐加密
     *
     * @param password
     * @return
     */
    public static String encryptPassword(String password) {
        return DigestUtils.md5Hex(String.format(CommonConst.SALT, password));
    }

    public static void main(String[] args) {
        System.out.println(encryptPassword("123456"));
    }
}
