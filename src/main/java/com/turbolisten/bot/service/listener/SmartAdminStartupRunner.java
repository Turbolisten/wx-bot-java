package com.turbolisten.bot.service.listener;

import com.turbolisten.bot.service.module.business.task.basic.TaskConfigService;
import io.github.wechaty.Wechaty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * 应用启动加载
 *
 * @author zhuo
 * @version 1.0
 * @since JDK1.8
 */
@Slf4j
@Component
public class SmartAdminStartupRunner implements CommandLineRunner, DisposableBean {

    @Autowired
    private Wechaty wechaty;

    @Autowired
    private TaskConfigService taskConfigService;

    @Override
    public void run(String... args) {

        log.info("###################### init start ######################");

       // wechaty.start(false);

        log.info("###################### init complete ######################");

        taskConfigService.initTask();
    }

    @Override
    public void destroy() {
        wechaty.stop();
    }
}