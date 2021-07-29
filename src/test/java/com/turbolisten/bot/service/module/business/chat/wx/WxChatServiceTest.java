package com.turbolisten.bot.service.module.business.chat.wx;

import com.turbolisten.bot.service.WxBotApplicationTests;
import com.turbolisten.bot.service.module.business.chat.wx.domain.WxChatResultDTO;
import com.turbolisten.bot.service.module.business.chat.wx.domain.WxChatResultMsgBaseDTO;
import com.turbolisten.bot.service.module.business.chat.wx.domain.WxChatSignatureQueryDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class WxChatServiceTest extends WxBotApplicationTests {

    @Autowired
    private WxChatService wxChatService;

    @Test
    void chat() {
        WxChatSignatureQueryDTO queryDTO = new WxChatSignatureQueryDTO();
        queryDTO.setUserId("1015");
        queryDTO.setUserName("listen");
        WxChatResultDTO<? extends WxChatResultMsgBaseDTO> resultDTO = wxChatService.chat("周杰伦", queryDTO);
        System.out.println(resultDTO);
    }
}