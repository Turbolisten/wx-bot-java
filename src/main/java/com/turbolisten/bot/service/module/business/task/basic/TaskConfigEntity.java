package com.turbolisten.bot.service.module.business.task.basic;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 定时任务配置
 *
 * @author Turbolisten
 * @date 2021/7/11 16:23
 */
@Data
@TableName("t_task_config")
public class TaskConfigEntity {

    @TableId(type = IdType.AUTO)
    private Integer taskId;

    /**
     * 任务执行类名
     */
    private String taskClassName;

    /**
     * cron 表达式
     */
    private String cron;

    /**
     * 额外数据
     */
    private String data;

    /**
     * 是否启用
     */
    private Boolean enabledFlag;

    /**
     * 任务描述
     */
    private String taskDesc;

    private LocalDateTime updateTime;

    private LocalDateTime createTime;
}
