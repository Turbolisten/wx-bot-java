package com.turbolisten.bot.service.module.business.commonword;

import com.turbolisten.bot.service.WxBotApplicationTests;
import com.turbolisten.bot.service.module.business.commonword.constant.CommonWordDataTypeEnum;
import com.turbolisten.bot.service.module.business.commonword.constant.CommonWordSceneTypeEnum;
import com.turbolisten.bot.service.module.business.commonword.domain.CommonWordAddDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class CommonWordServiceTest extends WxBotApplicationTests {

    @Autowired
    private CommonWordService commonWordService;

    @Test
    void addTest() {
        CommonWordAddDTO addDTO = new CommonWordAddDTO();
        addDTO.setContent("可爱的女人");
        addDTO.setSceneType(CommonWordSceneTypeEnum.TU_WEI.getValue());
        addDTO.setData("null");
        addDTO.setDataType(CommonWordDataTypeEnum.TEXT.getValue());
        commonWordService.add(addDTO);
    }
}