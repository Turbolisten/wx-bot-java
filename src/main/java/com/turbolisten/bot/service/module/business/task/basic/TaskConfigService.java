package com.turbolisten.bot.service.module.business.task.basic;

import com.turbolisten.bot.service.config.SchedulingConfig;
import com.turbolisten.bot.service.module.business.bot.WxBotSendMsgService;
import com.turbolisten.bot.service.module.business.chat.wx.WxChatService;
import com.turbolisten.bot.service.module.business.commonword.CommonWordService;
import com.turbolisten.bot.service.module.business.task.clock.ClockMsgTask;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.config.CronTask;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 定时任务 业务类
 *
 * @author Turbolisten
 * @date 2021/7/11 16:29
 */
@Slf4j
@Service
public class TaskConfigService {

    @Autowired
    private SchedulingConfig schedulingConfig;

    @Autowired
    private TaskConfigDao taskConfigDao;

    @Autowired
    private ApplicationContext context;

    @Autowired
    private CommonWordService commonWordService;

    @Autowired
    private WxChatService wxChatService;

    @Autowired
    private WxBotSendMsgService botSendMsgService;

    private ConcurrentHashMap<Integer, TaskConfigEntity> configMap = new ConcurrentHashMap<>();

    @Scheduled(cron = "15 10/3 * * * ?")
    public void syncTask() {
        // 刷新定时任务配置
        this.initTask();
    }

    public void initTask() {
        List<TaskConfigEntity> taskEntityList = taskConfigDao.query(null);
        if (CollectionUtils.isEmpty(taskEntityList)) {
            return;
        }

        for (TaskConfigEntity configEntity : taskEntityList) {
            Integer taskId = configEntity.getTaskId();
            TaskConfigEntity taskConfigTemp = configMap.get(taskId);
            String taskClassName = configEntity.getTaskClassName();
            String cron = configEntity.getCron();
            Boolean enabledFlag = configEntity.getEnabledFlag();
            if (null != taskConfigTemp) {
                // 比较任务是否变化
                if (Objects.equals(taskClassName, taskConfigTemp.getTaskClassName())
                        && Objects.equals(cron, taskConfigTemp.getCron())
                        && Objects.equals(configEntity.getData(), taskConfigTemp.getData())
                        && Objects.equals(enabledFlag, taskConfigTemp.getEnabledFlag())) {
                    continue;
                }
            }

            // 刷新缓存
            configMap.put(taskId, configEntity);

            if (!enabledFlag) {
                schedulingConfig.cancelTask(taskId.toString());
                log.info("--------------- Task -> {} stop ----------------", configEntity.getTaskDesc());
                return;
            }

            Class<?> clazz;
            try {
                clazz = Class.forName(taskClassName);
            } catch (ClassNotFoundException e) {
                throw new IllegalArgumentException("task config class name error:" + e);
            } catch (BeansException e) {
                throw new IllegalArgumentException(taskClassName + "task config class not in spring manager", e);
            }
            /**
             * 手动判断任务类型
             */
            Runnable runnable = null;
            if (clazz.isAssignableFrom(ClockMsgTask.class)) {
                // 微信闹钟定时任务
                runnable = new ClockMsgTask(commonWordService, wxChatService, botSendMsgService, configEntity);
            }

            if (null == runnable) {
                log.info("not handle task: " + configEntity);
                return;
            }

            schedulingConfig.resetTask(taskId.toString(), new CronTask(runnable, cron));

            log.info("--------------- Task -> {} refresh ----------------", configEntity.getTaskDesc());
        }
    }
}
