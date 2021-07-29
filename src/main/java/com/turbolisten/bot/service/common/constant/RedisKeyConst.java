package com.turbolisten.bot.service.common.constant;

/**
 * redis key 常量类
 *
 * @author listen
 * @date 2019/09/23 20:48
 */
public class RedisKeyConst {

    public static final String SEPARATOR = ":";

    public class Core {

        private static final String PROJECT = "bot:";

        public static final String LOCK = PROJECT + "lock:";

        public static final String TOKEN_USER = PROJECT + "token:user:";

    }
}
