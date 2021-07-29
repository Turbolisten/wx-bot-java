package com.turbolisten.bot.service.module.business.question.question.constant;


import com.turbolisten.bot.service.common.constant.BaseEnum;

/**
 * 试题标签类型枚举类
 *
 * @author liyingwu@163.com
 * @date 2020-02-05 12:42
 */
public enum QuestionTagTypeEnum implements BaseEnum {
    
    OFTEN_TEST(1, "常考题"),
    
    ERROR_PRONE(2, "易错题"),
    
    ;

    private Integer type;

    private String desc;

    QuestionTagTypeEnum(Integer type, String desc) {
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
