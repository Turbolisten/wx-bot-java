package com.turbolisten.bot.service.module.business.chat.wx;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.turbolisten.bot.service.module.business.bot.WxBotExceptionHandle;
import com.turbolisten.bot.service.module.business.chat.wx.domain.*;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * 微信对话平台
 *
 * @author Turbolisten
 * @date 2019/12/07 16:40
 */
@Slf4j
@Service
public class WxChatService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private WxChatTokenService tokenService;

    /**
     * 请求api
     * 对话回复
     */
    public WxChatResultDTO<? extends WxChatResultMsgBaseDTO> chat(String msg, WxChatSignatureQueryDTO queryDTO) {
        // 查询用户签名
        String signature = tokenService.getSignature(queryDTO);

        Map<String, String> param = new HashMap<>(10);
        param.put("signature", signature);
        // 对话内容
        param.put("query", msg);
        // 默认是online, debug是测试环境,online是线上环境
        param.put("env", "online");
        // 限定技能命中范围 比如：["技能1"]，只匹配命中“技能1”中的所有问答内容
        param.put("first_priority_skills", null);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> httpEntity = new HttpEntity<>(JSON.toJSONString(param), httpHeaders);

        ResponseEntity<String> res = restTemplate.postForEntity(WxChatConst.URL_CHAT + WxChatConst.TOKEN, httpEntity, String.class);
        if (res.getStatusCode() != HttpStatus.OK) {
            return null;
        }

        String body = res.getBody();
        // 解析为这个类 是因为属性少点
        WxChatResultTempDTO tempDTO = JSON.parseObject(body, WxChatResultTempDTO.class);
        if (StringUtils.isNotBlank(tempDTO.getErrMsg())) {
            WxBotExceptionHandle.sendErrMsg(null, "wx chat result msg error : " + tempDTO.getErrMsg() + " msg->" + msg);
            return null;
        }
        switch (tempDTO.answerType) {
            case "music":
                WxChatResultDTO<WxChatResultMusicDTO> music = JSON.parseObject(body,
                        new TypeReference<WxChatResultDTO<WxChatResultMusicDTO>>() {
                        });
                return music;
            case "text":
                WxChatResultDTO<WxChatResultTextDTO> text = JSON.parseObject(body,
                        new TypeReference<WxChatResultDTO<WxChatResultTextDTO>>() {
                        });
                return text;
            default:
                log.info("wx chat not handle msg type");
                WxBotExceptionHandle.sendErrMsg(null, "wx chat not handle msg type : " + tempDTO.answerType);
        }
        return null;
    }

    /**
     * 结果临时类
     */
    @Data
    private static class WxChatResultTempDTO {

        private Integer ansNodeId;

        private String ansNodeName;

        private String answer;

        private String answerType;

        private String errMsg;
    }

}
