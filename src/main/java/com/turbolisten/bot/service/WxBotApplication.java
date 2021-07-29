package com.turbolisten.bot.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 启动类
 *
 * @date 2021年7月9日 18:10:12
 */
@EnableScheduling
@EnableAsync
@SpringBootApplication
public class WxBotApplication {

    public static void main(String[] args) {
        SpringApplication.run(WxBotApplication.class, args);
        System.out.println("################# start-up finish #################");
    }

}
