package com.turbolisten.bot.service.module.business.bot;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.github.wechaty.user.Contact;
import io.github.wechaty.user.manager.ContactManager;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 异常 处理
 *
 * @author Turbolisten
 * @date 2021/7/11 12:13
 */
public class WxBotExceptionHandle {

    private static final List<String> WX_ID_LIST;

    private static ThreadPoolExecutor threadPool;

    static {
        ThreadFactory threadFactory = new ThreadFactoryBuilder()
                .setNameFormat("send-err-pool-%d")
                .build();
        threadPool = new ThreadPoolExecutor(1, 1, 10, TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(), threadFactory);

        // 接收消息人or群组id
        WX_ID_LIST = new ArrayList<>(10);
        WX_ID_LIST.add("turbolisten");
    }

    /**
     * 发送异常消息到微信群
     */
    public static void sendErrMsg(Throwable e, String... prefix) {
        threadPool.execute(() -> {
            StringBuilder msg = new StringBuilder("❌ 天塌了~");
            if (null != prefix && prefix.length > 0) {
                msg.append("\n" + Arrays.toString(prefix));
            }
            // 获取异常堆栈信息
            if (null != e) {
                StringWriter stringWriter = new StringWriter();
                e.printStackTrace(new PrintWriter(stringWriter, true));
                String errMsg = stringWriter.getBuffer().toString();
                msg.append("\n" + errMsg);
            }

            ContactManager contactManager = WxBotConfig.WECHATY.getContactManager();
            WX_ID_LIST.forEach(id -> {
                Contact contact = contactManager.load(id);
                contact.say(msg.toString());
            });
        });
    }
}
