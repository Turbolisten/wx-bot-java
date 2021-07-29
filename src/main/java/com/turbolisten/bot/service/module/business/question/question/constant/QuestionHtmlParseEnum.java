package com.turbolisten.bot.service.module.business.question.question.constant;


import com.turbolisten.bot.service.common.constant.BaseEnum;

/**
 * [  ]
 *
 * @author yandanyang
 * @date 2020/10/29 21:42
 */
public enum QuestionHtmlParseEnum implements BaseEnum {

    TITLE(0, "标题"),

    QUESTION(1, "试题"),

    OPTION(2, "选项"),

    ANSWER(3, "答案"),

    ANALYSIS(4, "解析"),

    CHILD_QUESTION(5, "子试题"),

    QUESTION_RANGE(6,"试题范围"),
    ;

    private Integer type;

    private String desc;

    private Integer level;

    QuestionHtmlParseEnum(Integer type, String desc) {
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