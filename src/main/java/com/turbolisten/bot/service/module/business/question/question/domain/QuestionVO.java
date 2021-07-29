package com.turbolisten.bot.service.module.business.question.question.domain;

import com.turbolisten.bot.service.module.business.question.question.constant.QuestionTagTypeEnum;
import com.turbolisten.bot.service.module.business.question.question.constant.QuestionTypeEnum;
import lombok.Data;

import java.util.List;

/**
 * @author Turbolisten
 * @date 2021/7/17 21:45
 */
@Data
public class QuestionVO {

    /**
     * 试题编号
     */
    private Integer questionNo;

    /**
     * 父级分类id
     */
    private Long parentCatalogId;

    /**
     * 父级分类名称
     */
    private String parentCatalogName;

    /**
     * 分类名称
     */
    private String catalogName;

    /**
     * 试题id
     */
    private Long questionId;

    /**
     * 试题目录ID
     */
    private Long catalogId;

    /**
     * 试题题干
     */
    private String questionTitle;

    /**
     * 试题分值
     */
    private Integer point;

    /**
     * 试题类型
     *
     * @see QuestionTypeEnum
     */
    private Integer questionType;

    /**
     * 是否正确 针对判断题
     */
    private Boolean rightFlag;

    /**
     * 做题正确数
     */
    private Integer answerRightNum;

    /**
     * 做题错误数
     */
    private Integer answerWrongNum;

    /**
     * 答案解析
     */
    private String questionAnalysis;

    /**
     * 试题标签(逗号分隔)
     *
     * @see QuestionTagTypeEnum
     */
    private String questionTag;

    private String remark;

    private List<QuestionOptionVO> optionList;
}
