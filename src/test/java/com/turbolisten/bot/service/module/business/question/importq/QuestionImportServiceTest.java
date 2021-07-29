package com.turbolisten.bot.service.module.business.question.importq;

import com.turbolisten.bot.service.WxBotApplicationTests;
import com.turbolisten.bot.service.common.domain.ResponseDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class QuestionImportServiceTest extends WxBotApplicationTests {

    @Autowired
    private QuestionImportService importService;

    @Test
    void importQuestion() {
        String path = "C:\\Users\\Turbolisten\\Desktop\\question\\练习_中级经济法-侯永斌（2021年）_基础精讲_侯永斌\\Word\\test.txt";
        Long catalogId = 20L;
        ResponseDTO<String> res = importService.importQuestion(path, catalogId);
        System.out.println("------------------> " + res.getMsg());
    }
}