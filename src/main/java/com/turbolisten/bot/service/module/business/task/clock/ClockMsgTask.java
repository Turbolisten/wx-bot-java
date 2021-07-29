package com.turbolisten.bot.service.module.business.task.clock;

import com.google.common.collect.Lists;
import com.turbolisten.bot.service.module.business.bot.WxBotConst;
import com.turbolisten.bot.service.module.business.bot.WxBotSendMsgService;
import com.turbolisten.bot.service.module.business.chat.wx.WxChatService;
import com.turbolisten.bot.service.module.business.chat.wx.domain.WxChatResultDTO;
import com.turbolisten.bot.service.module.business.chat.wx.domain.WxChatResultMsgBaseDTO;
import com.turbolisten.bot.service.module.business.chat.wx.domain.WxChatSignatureQueryDTO;
import com.turbolisten.bot.service.module.business.commonword.CommonWordService;
import com.turbolisten.bot.service.module.business.commonword.constant.CommonWordSceneTypeEnum;
import com.turbolisten.bot.service.module.business.commonword.domain.CommonWordDTO;
import com.turbolisten.bot.service.module.business.task.basic.TaskConfigEntity;

import java.util.List;

/**
 * 微信闹钟消息任务
 *
 * @author Turbolisten
 * @date 2021/7/11 14:16
 */
public class ClockMsgTask implements Runnable {

    private CommonWordService commonWordService;

    private WxChatService wxChatService;

    private WxBotSendMsgService botSendMsgService;

    private TaskConfigEntity taskConfig;

    public ClockMsgTask(CommonWordService commonWordService,
                        WxChatService wxChatService,
                        WxBotSendMsgService botSendMsgService,
                        TaskConfigEntity taskConfig) {
        this.commonWordService = commonWordService;
        this.wxChatService = wxChatService;
        this.botSendMsgService = botSendMsgService;
        this.taskConfig = taskConfig;
    }

    /**
     * 发送微信闹钟提示
     */
    @Override
    public void run() {

        List msgList = Lists.newArrayList();

        // 查询文案
        CommonWordDTO commonWordDTO = commonWordService.randomOne(CommonWordSceneTypeEnum.TU_WEI);
        if (null != commonWordDTO) {
            msgList.add(WxBotConst.Emoji.AI_XIN + commonWordDTO.getContent());
        }

        msgList.add(WxBotConst.Emoji.SHAN_DIAN + "Biu ~ 亲，起床啦 \n" + WxBotConst.Emoji.TAI_YANG + "地球不能没有你，人类还等着你去拯救啊");

        // 查询天气
        String weather = this.queryWeather();
        msgList.add(weather);

        // data 放的是微信联系人id
        botSendMsgService.sendMsg(taskConfig.getData(), msgList);
    }

    /**
     * 查询天气
     *
     * @return
     */
    private String queryWeather() {
        // 先固定查询洛阳天气
        String city = "洛阳天气";
        WxChatSignatureQueryDTO queryDTO = new WxChatSignatureQueryDTO();
        queryDTO.setUserId("clock");
        queryDTO.setUserName("clock");
        WxChatResultDTO<? extends WxChatResultMsgBaseDTO> resultDTO = wxChatService.chat(city, queryDTO);
        return resultDTO.getAnswer();
    }
}
