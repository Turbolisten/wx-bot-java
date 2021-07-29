package com.turbolisten.bot.service.module.business.commonword.domain;

import com.turbolisten.bot.service.module.business.commonword.constant.CommonWordDataTypeEnum;
import com.turbolisten.bot.service.module.business.commonword.constant.CommonWordSceneTypeEnum;
import lombok.Data;

/**
 * 常用文案句子
 *
 * @author Turbolisten
 * @date 2021/7/11 13:16
 */
@Data
public class CommonWordDTO {

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
}
