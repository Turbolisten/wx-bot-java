package com.turbolisten.bot.service.module.business.chat.wx.domain;

import lombok.Data;

/**
 * 微信对话 签名 查询
 * 设置了用户信息 在微信对话平台 就可以查询用户的对话统计分析
 *
 * @author Turbolisten
 * @date 2021/7/10 16:16
 */
@Data
public class WxChatSignatureQueryDTO {

    /**
     * 用户id
     */
    private String userId;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 用户头像
     */
    private String avatar;

}
