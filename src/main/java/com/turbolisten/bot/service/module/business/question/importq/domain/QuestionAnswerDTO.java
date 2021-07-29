package com.turbolisten.bot.service.module.business.question.importq.domain;

import lombok.Data;

/**
 * 题目
 *
 * @author Turbolisten
 * @date 2021/7/16 23:50
 */
@Data
public class QuestionAnswerDTO {

    private Long no;

    /**
     * 正确选项
     */
    private String rightOption;

    /**
     * 答案解析
     */
    private String analysis;
}
