package com.turbolisten.bot.service.module.business.question.answerstatistics.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 答题 统计记录
 *
 * @author Turbolisten
 * @date 2021/7/19 22:00
 */
@Data
@TableName("t_question_answer_statistics")
public class QuestionAnswerStatisticsEntity {

    @TableId(type = IdType.AUTO)
    private Long recordId;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 试题id
     */
    private Long questionId;

    /**
     * 答题次数
     */
    private Integer answerCount;

    /**
     * 错误次数
     */
    private Integer wrongCount;

    private LocalDateTime updateTime;

    private LocalDateTime createTime;
}
