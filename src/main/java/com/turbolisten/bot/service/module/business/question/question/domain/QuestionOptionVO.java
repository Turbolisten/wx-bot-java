package com.turbolisten.bot.service.module.business.question.question.domain;

import lombok.Data;

/**
 * 试题选项 VO
 *
 * @author listen
 * @date 2020-02-05 12:39
 */
@Data
public class QuestionOptionVO {

    private Long optionId;

    /**
     * 试题ID
     */
    private Long questionId;

    /**
     * 选项标签
     */
    private String optionTag;

    /**
     * 选项名称
     */
    private String optionName;

    /**
     * 是否正确
     */
    private Boolean rightFlag;
}
