package com.turbolisten.bot.service.module.business.question.catalog;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * [ 试题目录 ]
 *
 * @author yandanyang
 * @date 2020/10/28 20:24
 */
@Data
@TableName("t_question_catalog")
public class QuestionCatalogEntity {

    @TableId(type = IdType.AUTO)
    private Long catalogId;

    /**
     * 目录名称
     */
    private String catalogName;

    /**
     * 父节点id
     */
    private Long parentId;

    private Integer sort;

    private String remark;

    private Boolean deletedFlag;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
