package com.turbolisten.bot.service.module.business.chat.wx.domain;

/**
 * 微信
 *
 * @author Turbolisten
 * @date 2021/7/10 16:13
 */
public class WxChatConst {

    public static final String TOKEN = "jCip7YwWTs1zhUgvSaNhF68S1AbglR";

    /**
     * 匹配状态
     */
    public static final String NO_MATCH_STATUS = "NOMATCH";

    /**
     * 没有匹配技能的id
     */
    public static final int NO_MATCH_ANS_NODE_ID = 0;

    /**
     * 获取 微信对话平台token url
     */
    public static final String URL_GET_SIGNATURE = "https://openai.weixin.qq.com/openapi/sign/";

    /**
     * 对话回复 url
     */
    public static final String URL_CHAT = "https://openai.weixin.qq.com/openapi/aibot/";
}
