package com.turbolisten.bot.service.module.business.chat.wx.domain;

import lombok.Data;

/**
 * 微信对话回复 音乐类型 DTO
 *
 * @author Turbolisten
 * @date 2021/7/10 16:49
 */
@Data
public class WxChatResultMusicDTO extends WxChatResultMsgBaseDTO {

    private String albumName;

    private String article;

    private Integer confidence;

    private String event;

    private Boolean listOptions;

    private String musicUrl;

    private String opening;

    private String picUrl;

    private Long requestId;

    private String respTitle;

    private String sceneStatus;

    private String sessionId;

    private String singerName;

    private String songName;

    private Boolean takeOptionsOnly;
}
