package com.turbolisten.bot.service.common.constant;

import java.util.*;

/**
 * [ 通用常量 ]
 *
 * @author yandanyang
 * @version 1.0
 * @company 钢圈
 * @copyright (c) 2019 钢圈Inc. All rights reserved.
 * @date
 * @since JDK1.8
 */
public class CommonConst {

    public static final long ONE = 1;

    /**
     * salt
     */
    public static final String SALT = "gzys_%s";

    /**
     * 全局通用分隔符
     */
    public static final String SEPARATOR = ",";

    /**
     * 全局通用分隔符 下划线
     */
    public static final String UNDERLINE = "_";

    /**
     * 全局通用分隔符
     */
    public static final Character SEPARATOR_CHAR = ',';

    /**
     * 空字符串
     */
    public static final String EMPTY_STR = "";

    /**
     * 空MaP
     */
    public static final Map EMPTY_MAP = Collections.unmodifiableMap(new HashMap<>(0));

    /**
     * 空 list
     */
    public static final List EMPTY_LIST = Collections.unmodifiableList(new ArrayList<>(0));

}
