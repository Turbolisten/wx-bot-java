package com.turbolisten.bot.service.module.business.chat;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Lists;
import com.turbolisten.bot.service.module.business.bot.WxBotConst;
import com.turbolisten.bot.service.module.business.bot.WxBotSendMsgService;
import com.turbolisten.bot.service.module.business.chat.qingyunke.QingYunKeChatService;
import com.turbolisten.bot.service.module.business.chat.question.BotQuestionService;
import com.turbolisten.bot.service.module.business.chat.wx.WxChatService;
import com.turbolisten.bot.service.module.business.chat.wx.domain.*;
import com.turbolisten.bot.service.module.business.commonword.CommonWordService;
import com.turbolisten.bot.service.module.business.commonword.constant.CommonWordSceneTypeEnum;
import io.github.wechaty.schemas.UrlLinkPayload;
import io.github.wechaty.user.Contact;
import io.github.wechaty.user.Message;
import io.github.wechaty.user.Room;
import io.github.wechaty.user.UrlLink;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 机器人对话 业务
 *
 * @author Turbolisten
 * @date 2021/7/10 15:45
 */
@Slf4j
@Service
public class BotChatService {

    @Autowired
    private QingYunKeChatService qingYunKeChatService;

    @Autowired
    private WxBotSendMsgService sendMsgService;

    @Autowired
    private WxChatService wxChatService;

    @Autowired
    private CommonWordService commonWordService;

    @Autowired
    private BotQuestionService botQuestionService;

    /**
     * 当前用户 最后1次对话缓存
     */
    private Cache<String, Long> USER_LAST_CHAT_CACHE = CacheBuilder.newBuilder().expireAfterWrite(8, TimeUnit.HOURS).build();

    /**
     * @param fromWxId
     * @param message
     * @return
     */
    private boolean isFirstChat(String fromWxId, Message message) {
        // 判断首次聊天 发送提示信息
        Long lastTime = USER_LAST_CHAT_CACHE.getIfPresent(fromWxId);
        // 更新最后1次对话缓存
        USER_LAST_CHAT_CACHE.put(fromWxId, System.currentTimeMillis() / 1000);
        return null == lastTime;
    }

    /**
     * 回复对话
     *
     * @param msg
     * @return
     */
    public void reply(String msg, Message message) {
        // 获取当前来源微信id
        Room room = message.room();
        boolean isRoom = null != room;
        String fromWxId = isRoom ? room.getId() : message.from().getId();

        // 是否首次聊天
        boolean firstChat = this.isFirstChat(fromWxId, message);
        if (firstChat) {
            StringBuilder sb = new StringBuilder();
            sb.append(WxBotConst.Emoji.FA_DAI + "嘀嘀嘀~\n")
                    .append(WxBotConst.Emoji.DE_YI + "主人不在家，我是机器人小管家铁柱\n")
                    .append(WxBotConst.Emoji.WANG_CHAI + "当然我也可以陪您聊天");
            sendMsgService.sendMsg(message, sb.toString());
            return;
        }

        msg = StringUtils.trim(msg);
        if (StringUtils.isBlank(msg)) {
            sendMsgService.sendMsg(message, commonWordService.randomOne(CommonWordSceneTypeEnum.DU_JI_TANG).getContent());
            return;
        }
        // 判断当前模式 默认闲聊
        ChatModel.ModelEnum modelEnum = ChatModel.getModel(fromWxId);
        modelEnum = null == modelEnum ? ChatModel.ModelEnum.CHAT : modelEnum;
        switch (modelEnum) {
            case CHAT:
                break;
            case QUESTION:
                // 答题模式
                switch (msg) {
                    case "退出刷题":
                        botQuestionService.exitQuestionModel(fromWxId, message);
                        return;
                    default:
                        // 当作试题回答处理
                        botQuestionService.handleQuestionAnswer(fromWxId, msg, message);
                }
                return;
            default:
        }

        /**
         * 判断是否包含关键指令
         */
        switch (msg) {
            case "id":
                // 查询微信id
                String reply = isRoom ? "当前群聊id：" + fromWxId : "您的微信id：" + fromWxId;
                sendMsgService.sendMsg(message, reply);
                return;
            case "祖安语录":
                sendMsgService.sendMsg(message, commonWordService.randomOne(CommonWordSceneTypeEnum.NMSL).getContent());
                return;
            case "我要刷题":
            case "刷题":
                // 进入刷题模式
                botQuestionService.enterQuestionModel(fromWxId, message);
                return;
            default:
                // 未处理
        }

        // 优先使用微信对话api
        String wxId;
        String wxName;
        if (isRoom) {
            wxId = room.getId();
            try {
                wxName = room.getTopic().get();
            } catch (Exception e) {
                log.error("wx bot query room topic error :", e);
                return;
            }
        } else {
            Contact contact = message.from();
            wxId = contact.getId();
            wxName = contact.name();
        }
        WxChatSignatureQueryDTO signatureQueryDTO = new WxChatSignatureQueryDTO();
        signatureQueryDTO.setUserId(wxId);
        signatureQueryDTO.setUserName(wxName);
        WxChatResultDTO<? extends WxChatResultMsgBaseDTO> chatResultDTO = wxChatService.chat(msg, signatureQueryDTO);
        if (null != chatResultDTO) {
            // 判断回复结果
            List<? extends WxChatResultMsgBaseDTO> resultMsgList = chatResultDTO.getMsg();
            if (chatResultDTO.getAnsNodeId() != WxChatConst.NO_MATCH_ANS_NODE_ID && CollectionUtils.isNotEmpty(resultMsgList)) {
                // 第一个回复
                List replyMsgList = Lists.newArrayList();
                String firstAnswer = chatResultDTO.getAnswer();
                replyMsgList.add(firstAnswer);

                /**
                 * 循环发送后续消息
                 * 判断消息类型
                 */
                for (WxChatResultMsgBaseDTO msgBaseDTO : resultMsgList) {
                    if (msgBaseDTO instanceof WxChatResultTextDTO) {
                        // 文本
                        WxChatResultTextDTO textDTO = (WxChatResultTextDTO) msgBaseDTO;
                        String content = textDTO.getContent();
                        if (!Objects.equals(firstAnswer, content)) {
                            replyMsgList.add(content);
                        }
                    }
                    if (msgBaseDTO instanceof WxChatResultMusicDTO) {
                        // 音乐 发送链接
                        WxChatResultMusicDTO musicDTO = (WxChatResultMusicDTO) msgBaseDTO;
                        UrlLinkPayload urlLinkPayload = new UrlLinkPayload(musicDTO.getSongName(), musicDTO.getMusicUrl());
                        urlLinkPayload.setDescription(musicDTO.getSingerName() + "\n" + musicDTO.getAlbumName());
                        urlLinkPayload.setThumbnailUrl(musicDTO.getPicUrl());
                        replyMsgList.add(new UrlLink(urlLinkPayload));
                    }
                }
                sendMsgService.sendMsgMulti(message, replyMsgList);
                return;
            }
        }

        // 网上野鸡 对话 api
        String chat = qingYunKeChatService.chat(msg);
        sendMsgService.sendMsg(message, chat);
    }
}
