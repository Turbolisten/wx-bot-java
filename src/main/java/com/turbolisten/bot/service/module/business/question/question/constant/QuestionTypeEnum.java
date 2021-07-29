package com.turbolisten.bot.service.module.business.question.question.constant;


import com.turbolisten.bot.service.common.constant.BaseEnum;

/**
 * 试题类型枚举类
 *
 * @author liyingwu@163.com
 * @date 2020-02-05 12:42
 */
public enum QuestionTypeEnum implements BaseEnum {

    /**
     * 单选题
     */
    SINGLE(1, "单选题"),

    /**
     * 多选题
     */
    MULTIPLE(2, "多选题"),

    /**
     * 共用题干试题
     */
    SHARE_QUESTION(3, "共用题干"),

    /**
     * 共用选项试题
     */
    SHARE_OPTION(4, "共用选项"),

    Q_A(5, "问答"),

    /**
     * 100 判断题
     */
    TRUE_FALSE(100, "判断题"),

    ;

    private Integer type;

    private String desc;

    QuestionTypeEnum(Integer type, String desc) {
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
