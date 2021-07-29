package com.turbolisten.bot.service.module.business.chat.wx.domain;

import lombok.Data;

import java.util.List;

/**
 * 微信对话回复 DTO
 *
 * @author Turbolisten
 * @date 2021/7/10 16:49
 */
@Data
public class WxChatResultDTO<T extends WxChatResultMsgBaseDTO> {

    private Integer ansNodeId;

    private String ansNodeName;

    private String answer;

    private Integer answerOpen;

    private String answerType;

    private String article;

    private Double confidence;

    private String createTime;

    private String dialogSessionStatus;

    private String dialogStatus;

    private String event;

    private String fromUserName;

    private String intentConfirmStatus;

    private Boolean isDefaultAnswer;

    private Boolean listOptions;

    private List<T> msg;

    private String msgId;

    private String opening;

    private Long requestId;

    private Integer ret;

    private String sceneStatus;

    private String sessionId;

    private String skillId;

    private String skillName;

/*    private List<?> slotInfo;

    private List<?> slotsInfo;*/

    private String status;

    private Boolean takeOptionsOnly;

    private String title;

    private String toUserName;

    private String query;

    private String rid;

}
