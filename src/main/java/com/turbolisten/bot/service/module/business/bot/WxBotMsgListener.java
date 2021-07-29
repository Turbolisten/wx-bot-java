package com.turbolisten.bot.service.module.business.bot;

import com.turbolisten.bot.service.module.business.chat.BotChatService;
import io.github.wechaty.MessageListener;
import io.github.wechaty.schemas.ContactType;
import io.github.wechaty.schemas.MessageType;
import io.github.wechaty.user.Contact;
import io.github.wechaty.user.Message;
import io.github.wechaty.user.Room;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RegExUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * 微信消息监听处理业务
 *
 * @author Turbolisten
 * @date 2021/7/10 12:24
 */
@Service
@Slf4j
public class WxBotMsgListener implements MessageListener {

    @Autowired
    private WxBotSendMsgService sendMsgService;

    @Autowired
    private BotChatService botChatService;

    /**
     * 消息处理
     *
     * @param message
     */
    @Override
    public void handler(Message message) {

        Contact contact = message.from();
        log.info("收到消息-> msg id ->{} wx id-> {} type->{} content-> {}", message.getId(), contact.getId(), message.type(), message.content());

        // 是否需要处理消息
        boolean needHandle = isNeedHandle(message);
        if (!needHandle) {
            return;
        }

        /**
         * 0.1.5 版本
         * 消息类型有bug
         * 视频消息类型 没有正确返回 暂不处理
         */
        MessageType messageType = message.type();
        switch (messageType) {
            case Text:
                String msg = message.text();
                // 是否群聊
                Room room = message.room();
                if (null != room) {
                    if (StringUtils.startsWith(message.text(), WxBotConst.Msg.AT_ALL)) {
                        // 不处理 at所有人
                        return;
                    }
                    try {
                        boolean self = message.mentionSelf();
                        if (!self) {
                            // 没有at自己
                            return;
                        }
                        // at多人时候 获取有bug
                        msg = message.mentionText();
                        msg = RegExUtils.removeAll(msg, AT_PATTERN);
                    } catch (Throwable e) {
                        //WxBotExceptionHandle.sendErrMsg(e, "大事不妙");
                        // 目前版本有bug 当接收到复制的at消息 获取at相关信息会报错 需要手动判断获取是否at
                        String atSelfMsg = getAtSelfMsg(msg, WxBotConfig.BOT_SELF.name());
                        if (null == atSelfMsg) {
                            return;
                        }
                        msg = atSelfMsg;
                    }
                }
                // 对话回复
                botChatService.reply(msg, message);
                break;
            case Recalled:
                // 撤回消息 这个方法有bug 暂时获取到	r, _ := message.ToRecalled()
                // msg := "[机智]大威天龙，铁柱什么都看到了~ 发个红包，当作无事发生 [红包]"
                // say(msg, message)
                log.info("收到撤回消息");
                break;
            case RedEnvelope:
                // 红包消息
                msg = "[红包]老板威武~ 祝老板紫气兆祥，财源滚滚！[庆祝]";
                sendMsgService.sendMsg(message, msg);
                break;
            case Unknown:
                // 未知消息
                break;
            case Attachment:
                // 文件消息
                break;
            case Audio:
                // 语音消息
                break;
            case Contact:
                // 联系人消息
                break;
            case ChatHistory:
                // 聊天记录消息
                break;
            case Emoticon:
                // 表情包消息
                break;
            case Image:
                // 图片消息
                break;
            case Location:
                // 地理位置消息
                break;
            case MiniProgram:
                // 小程序消息
                break;
            case Transfer:
                // 不知道啥消息
                break;
            case Url:
                // 链接消息
                break;
            case Video:
                // 视频消息
                break;
            default:
                log.info("not handle msg -> {}", messageType);
                return;
        }
    }

    /**
     * 检查是否需要处理的消息
     *
     * @param message
     * @return
     */
    private boolean isNeedHandle(Message message) {
        if (message.self()) {
            // 不处理自己的消息
            return false;
        }
        Contact contact = message.from();
        if (null == contact) {
            return false;
        }
        // 不处理公众号 和 未知消息
        if (ContactType.Official == contact.type() || ContactType.Unknown == contact.type()) {
            return false;
        }
        // 不处理微信团队消息
        if (Objects.equals(contact.getId(), WxBotConst.WxId.WX_TEAM)) {
            return false;
        }
        return true;
    }


    public static void main(String[] args) {
        String name = "NNNN";
        String msg = "@Bot @NNNN  你知道吗 @Bot你知道吗";
        // System.out.println(getAtSelfMsg(msg, name));

        String pattern = "@\\S+\\s";
        String msg1 = RegExUtils.removeAll(msg, pattern);
        String msg2 = RegExUtils.removeAll(msg, "@.+?(\\s)");
        System.out.println(msg1.trim());
        System.out.println(msg2.trim());
        System.out.println(getAtSelfMsg(msg, name));
    }


    /**
     * at 正则
     */
    private static Pattern AT_PATTERN = Pattern.compile("@.+?(\\ |\\s)");

    /**
     * 获取 at 自己的消息
     * 这个方法有待完善
     *
     * @param msg
     * @return 没有at 会返回null
     */
    private static String getAtSelfMsg(String msg, String name) {
        if (null == msg) {
            return null;
        }
        msg = RegExUtils.removeAll(msg, AT_PATTERN);
        return msg.trim();
    }
}
