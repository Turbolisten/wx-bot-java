package com.turbolisten.bot.service.module.business.bot;

import io.github.wechaty.LoginListener;
import io.github.wechaty.ScanListener;
import io.github.wechaty.Wechaty;
import io.github.wechaty.WechatyOptions;
import io.github.wechaty.io.github.wechaty.schemas.EventEnum;
import io.github.wechaty.schemas.PuppetOptions;
import io.github.wechaty.schemas.ScanStatus;
import io.github.wechaty.user.ContactSelf;
import io.github.wechaty.utils.QrcodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * bot config
 *
 * @author Turbolisten
 * @date 2021/7/9 17:30
 */
@Configuration
@Slf4j
public class WxBotConfig implements LoginListener, ScanListener {

    @Value("${wx-bot.token}")
    private String token;

    @Value("${wx-bot.server}")
    private String server;

    @Autowired
    private WxBotMsgListener msgListener;

    public static ContactSelf BOT_SELF;

    public static Wechaty WECHATY;

    @Bean
    public Wechaty initBot() {
        PuppetOptions puppetOptions = new PuppetOptions();
        puppetOptions.setToken(token);
        puppetOptions.setEndPoint(server);

        WechatyOptions options = new WechatyOptions();
        options.setPuppetOptions(puppetOptions);

        Wechaty wechaty = Wechaty.instance(options)
                .onScan(this)
                .onLogin(this)
                .onMessage(msgListener);

        wechaty.on(EventEnum.LOGOUT, objects -> {
            log.info("退出登录 -> {}", objects);
        });

        wechaty.on(EventEnum.HEART_BEAT, objects -> {
            log.info("心跳蹦哒哒 -> {}", objects);
        });

        return wechaty;
    }

    /**
     * 扫码监听
     *
     * @param qrcode
     * @param scanStatus
     * @param s1
     */
    @Override
    public void handler(String qrcode, ScanStatus scanStatus, String s1) {
        String qrCodeUrl = "https://wechaty.github.io/qrcode/" + QrcodeUtils.guardQrCodeValue(qrcode);
        log.info("scan qr code url -> {}", qrCodeUrl);
        log.info("扫码状态 -> {}", scanStatus);
    }

    /**
     * 登录成功监听
     *
     * @param contactSelf
     */
    @Override
    public void handler(ContactSelf contactSelf) {
        log.info("User login success : {}", contactSelf.name());
        BOT_SELF = contactSelf;
        WECHATY = contactSelf.getWechaty();
    }

}
