package com.turbolisten.bot.service.module.business.api;

import com.google.common.collect.Lists;
import com.turbolisten.bot.service.WxBotApplicationTests;
import com.turbolisten.bot.service.module.business.commonword.CommonWordService;
import com.turbolisten.bot.service.module.business.commonword.constant.CommonWordDataTypeEnum;
import com.turbolisten.bot.service.module.business.commonword.constant.CommonWordSceneTypeEnum;
import com.turbolisten.bot.service.module.business.commonword.domain.CommonWordAddDTO;
import com.turbolisten.bot.service.util.SmartRandomUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

class ShaDiaoWordApiTest extends WxBotApplicationTests {

    @Autowired
    private ShaDiaoWordApi shaDiaoWordApi;

    @Autowired
    private CommonWordService commonWordService;

    @Test
    void queryShaDiao() {
        String word = shaDiaoWordApi.queryShaDiao(ShaDiaoWordApi.ShaDiaoWordType.PYQ);
        System.out.println("--------> " + word);
    }

    /**
     * 爬虫抓取沙雕文案
     */
    @Test
    void spiderShaDiaoTest() {

        AtomicInteger num = new AtomicInteger(0);

        Runnable task = () -> {
            while (true) {


                ArrayList<ShaDiaoWordApi.ShaDiaoWordType> typeList = Lists.newArrayList(ShaDiaoWordApi.ShaDiaoWordType.values());
                ShaDiaoWordApi.ShaDiaoWordType wordType = SmartRandomUtil.randomOne(typeList);
                String content = shaDiaoWordApi.queryShaDiao(wordType);
                if (null == content) {
                    return;
                }

                CommonWordAddDTO addDTO = new CommonWordAddDTO();
                addDTO.setContent(content);
                addDTO.setData(null);
                addDTO.setDataType(CommonWordDataTypeEnum.TEXT.getValue());

                CommonWordSceneTypeEnum sceneType;
                switch (wordType) {
                    case CHP:
                    case PYQ:
                        sceneType = CommonWordSceneTypeEnum.TU_WEI;
                        break;
                    case NMSL:
                        // 祖安语录
                        sceneType = CommonWordSceneTypeEnum.NMSL;
                        break;
                    case DU:
                        sceneType = CommonWordSceneTypeEnum.DU_JI_TANG;
                        break;
                    default:
                        sceneType = CommonWordSceneTypeEnum.TU_WEI;
                }
                addDTO.setSceneType(sceneType.getValue());

                boolean result = commonWordService.add(addDTO);
                if (result) {
                    int num1 = num.incrementAndGet();
                    System.out.println("insert success count -> " + num1);
                }
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        for (int i = 0; i < 10; i++) {
            new Thread(task).start();
        }

        while (true) {

        }
    }
}