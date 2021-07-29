package com.turbolisten.bot.service.module.business.commonword.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.turbolisten.bot.service.module.business.commonword.constant.CommonWordDataTypeEnum;
import com.turbolisten.bot.service.module.business.commonword.constant.CommonWordSceneTypeEnum;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 常用文案橘子 实体类
 *
 * @author Turbolisten
 * @date 2021/7/11 13:16
 */
@Data
@TableName("t_common_word")
public class CommonWordEntity {

    @TableId(type = IdType.AUTO)
    private Integer wordId;

    /**
     * 语句内容
     */
    private String content;

    /**
     * 语句场景类型
     *
     * @see CommonWordSceneTypeEnum
     */
    private Integer sceneType;

    /**
     * 额外数据
     */
    private String data;

    /**
     * 数据类型
     *
     * @see CommonWordDataTypeEnum
     */
    private Integer dataType;

    private Boolean deletedFlag;

    private String remark;

    private LocalDateTime updateTime;

    private LocalDateTime createTime;
}
