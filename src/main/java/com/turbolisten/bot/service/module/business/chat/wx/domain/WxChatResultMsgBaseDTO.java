package com.turbolisten.bot.service.module.business.chat.wx.domain;

import lombok.Data;

/**
 * 微信对话回复 基础属性 DTO
 *
 * @author Turbolisten
 * @date 2021/7/10 16:49
 */
@Data
public class WxChatResultMsgBaseDTO {

    private Integer ansNodeId;

    private String ansNodeName;

    private String status;

    private String msgType;
}
