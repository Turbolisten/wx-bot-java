package com.turbolisten.bot.service.module.business.question.question.domain;

import com.turbolisten.bot.service.module.business.question.question.constant.QuestionTypeEnum;
import lombok.Data;

/**
 * 试题查询 DTO
 *
 * @author Turbolisten
 * @date 2021/7/17 21:45
 */
@Data
public class QuestionQueryDTO {

    /**
     * 父级分类id
     */
    private Long parentCatalogId;

    /**
     * 试题目录ID
     */
    private Long catalogId;

    /**
     * 试题类型
     *
     * @see QuestionTypeEnum
     */
    private Integer questionType;

    private Boolean publishFlag;

    private Boolean deletedFlag;

    private Integer limit;
}

