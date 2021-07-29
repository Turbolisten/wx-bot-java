package com.turbolisten.bot.service.module.business.chat.wx.domain;

import lombok.Data;

/**
 * 微信对话回复 文本 DTO
 *
 * @author Turbolisten
 * @date 2021/7/10 16:49
 */
@Data
public class WxChatResultTextDTO extends WxChatResultMsgBaseDTO {

    private Integer ansNodeIdX;
    private String ansNodeNameX;
    private String article;
    private Integer confidence;
    private String content;
    private String debugInfo;
    private String event;
    private Boolean listOptions;
    private String msgTypeX;
    private String opening;
    private Long requestId;
    private String respTitle;
    private String sceneStatus;
    private String sessionId;
    private Boolean takeOptionsOnly;
}
