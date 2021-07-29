package com.turbolisten.bot.service.module.business.chat.wx;

import com.alibaba.fastjson.JSON;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.turbolisten.bot.service.module.business.bot.WxBotExceptionHandle;
import com.turbolisten.bot.service.module.business.chat.wx.domain.WxChatConst;
import com.turbolisten.bot.service.module.business.chat.wx.domain.WxChatSignatureDTO;
import com.turbolisten.bot.service.module.business.chat.wx.domain.WxChatSignatureQueryDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * 微信对话平台
 *
 * @author Turbolisten
 * @date 2019/12/07 16:40
 */
@Slf4j
@Service
public class WxChatTokenService {

    @Autowired
    private RestTemplate restTemplate;

    /**
     * 微信对话 用户签名缓存
     */
    private static final Cache<String, String> SIGNATURE_CACHE = CacheBuilder.newBuilder()
            .expireAfterWrite(7100, TimeUnit.SECONDS)
            .build();

    /**
     * 获取微信签名 优先从缓存中
     *
     * @param queryDTO
     * @return
     */
    public String getSignature(WxChatSignatureQueryDTO queryDTO) {
        String signature = null;
        try {
            signature = SIGNATURE_CACHE.get(queryDTO.getUserId(), () -> querySignature(queryDTO).getSignature());
        } catch (ExecutionException e) {
            log.error("获取微信对话平台签名失败：", e);
            WxBotExceptionHandle.sendErrMsg(e, "获取微信对话平台签名失败");
        }
        return signature;
    }

    /**
     * 请求api
     * 获取微信签名
     */
    private WxChatSignatureDTO querySignature(WxChatSignatureQueryDTO queryDTO) {
        Map<String, String> param = new HashMap<>(10);
        param.put("userid", queryDTO.getUserId());
        param.put("username", queryDTO.getUserName());
        param.put("avatar", queryDTO.getAvatar());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> httpEntity = new HttpEntity<>(JSON.toJSONString(param), httpHeaders);
        ResponseEntity<String> res = restTemplate.postForEntity(WxChatConst.URL_GET_SIGNATURE + WxChatConst.TOKEN, httpEntity, String.class);
        if (res.getStatusCode() != HttpStatus.OK) {
            return null;
        }
        return JSON.parseObject(res.getBody(), WxChatSignatureDTO.class);
    }

}
