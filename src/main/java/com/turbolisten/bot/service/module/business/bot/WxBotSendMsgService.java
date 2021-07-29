package com.turbolisten.bot.service.module.business.bot;

import com.google.common.collect.Lists;
import io.github.wechaty.user.Contact;
import io.github.wechaty.user.Message;
import io.github.wechaty.user.Room;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 微信机器人发送消息
 *
 * @author Turbolisten
 * @date 2021/7/10 15:25
 */
@Service
public class WxBotSendMsgService {

    /**
     * 发送消息 给联系人 可以发送多条
     *
     * @param contractId
     * @param msgList
     */
    public void sendMsg(String contractId, List msgList) {
        Contact contact = WxBotConfig.WECHATY.getContactManager().load(contractId);
        msgList.forEach(contact::say);
    }

    /**
     * 发送消息 可以发送多条
     *
     * @param message
     * @param msg
     */
    public void sendMsg(Message message, Object... msg) {
        this.sendMsgMulti(message, Lists.newArrayList(msg));
    }

    /**
     * 发送消息 可以发送多条
     *
     * @param msgList
     * @param message
     */
    public void sendMsgMulti(Message message, List msgList, Long interval) {
        Contact from = message.from();
        Room room = message.room();
        boolean isRoom = null != room;

        msgList.forEach(msg -> {
            if (isRoom) {
                // 房间内 文本内容需要回复at对应人
                if (msg instanceof String) {
                    room.say(msg, Lists.newArrayList(from));
                } else {
                    // 其他内容 正常发送
                    room.say(msg);
                }
            } else {
                message.say(msg, from);
            }
            if (null != interval) {
                try {
                    Thread.sleep(interval);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 发送消息 可以发送多条
     *
     * @param msgList
     * @param message
     */
    public void sendMsgMulti(Message message, List msgList) {
        // 默认发送间隔 600毫秒
        this.sendMsgMulti(message, msgList, 600L);
    }

}
