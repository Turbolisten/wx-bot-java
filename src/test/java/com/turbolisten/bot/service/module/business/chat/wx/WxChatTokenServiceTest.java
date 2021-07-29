package com.turbolisten.bot.service.module.business.chat.wx;

import com.turbolisten.bot.service.WxBotApplicationTests;
import com.turbolisten.bot.service.module.business.chat.wx.domain.WxChatSignatureQueryDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class WxChatTokenServiceTest extends WxBotApplicationTests {

    @Autowired
    private WxChatTokenService wxChatTokenService;

    @Test
    void getSignature() {
        WxChatSignatureQueryDTO queryDTO = new WxChatSignatureQueryDTO();
        queryDTO.setUserId("1015");
        queryDTO.setUserName("listen");
        String signature = wxChatTokenService.getSignature(queryDTO);
        System.out.println(signature);
        signature = wxChatTokenService.getSignature(queryDTO);
        System.out.println(signature);
    }
}