package com.turbolisten.bot.service.module.business.question.question.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 试题选项 对象
 *
 * @author liyingwu <liushui05@qq.com>
 * @date 2020-02-05 12:39
 */
@Data
@TableName("t_question_option")
public class QuestionOptionEntity {

    @TableId(type = IdType.AUTO)
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

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
