package com.turbolisten.bot.service.module.business.commonword.constant;

import com.turbolisten.bot.service.common.constant.BaseEnum;

/**
 * 文案场景类型 枚举类
 *
 * @author listen
 * @date 2021/07/11 10:50
 */
public enum CommonWordSceneTypeEnum implements BaseEnum {

    /**
     * 0 通用
     */
    COMMON(0, "通用"),

    /**
     * 1 土味
     */
    TU_WEI(1, "土味文案"),

    /**
     * 2 祖安语录
     */
    NMSL(2, "祖安语录"),

    /**
     * 3 毒鸡汤
     */
    DU_JI_TANG(3, "毒鸡汤"),

    ;
    private Integer type;

    private String desc;

    CommonWordSceneTypeEnum(Integer type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    /**
     * 获取枚举类的值
     *
     * @return Integer
     */
    @Override
    public Integer getValue() {
        return type;
    }

    /**
     * 获取枚举类的说明
     *
     * @return String
     */
    @Override
    public String getDesc() {
        return desc;
    }
}
