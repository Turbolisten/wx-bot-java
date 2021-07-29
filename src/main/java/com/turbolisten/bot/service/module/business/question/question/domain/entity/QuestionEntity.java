package com.turbolisten.bot.service.module.business.question.question.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.turbolisten.bot.service.module.business.question.question.constant.QuestionTagTypeEnum;
import com.turbolisten.bot.service.module.business.question.question.constant.QuestionTypeEnum;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 试题 数据源对象
 *
 * @author liyingwu <liushui05@qq.com>
 * @date 2020-02-05 12:48
 */
@Data
@TableName("t_question")
public class QuestionEntity {

    @TableId(type = IdType.AUTO)
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
     * 发布标记
     */
    private Boolean publishFlag;

    /**
     * 试题标签(逗号分隔)
     *
     * @see QuestionTagTypeEnum
     */
    private String questionTag;

    private String remark;

    private Boolean deletedFlag;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
