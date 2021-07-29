package com.turbolisten.bot.service.module.business.chat.wx.domain;

import lombok.Data;

/**
 * 微信对话 用户签名
 *
 * @author Turbolisten
 * @date 2021/7/10 16:16
 */
@Data
public class WxChatSignatureDTO {

    /**
     * 用户id
     */
    private String userId;

    /**
     * 用户签名
     */
    private String signature;

    /**
     * 过期时间
     */
    private Integer expiresIn;

}
