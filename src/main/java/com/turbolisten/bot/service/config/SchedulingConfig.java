package com.turbolisten.bot.service.config;

import com.turbolisten.bot.service.util.SmartBeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.SchedulingException;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.config.Task;
import org.springframework.scheduling.config.TriggerTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.stream.Collectors;

/**
 * 定时任务配置
 *
 * @author zhuoda
 * @Date 2020/5/22
 */
@Slf4j
@Configuration
public class SchedulingConfig implements SchedulingConfigurer {

    private ScheduledTaskRegistrar taskRegistrar;

    private Set<ScheduledFuture<?>> scheduledFutures = null;

    private final Map<String, ScheduledFuture<?>> taskFutures = new ConcurrentHashMap<>();

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        this.taskRegistrar = taskRegistrar;
    }

    private Set<ScheduledFuture<?>> getScheduledFutures() {
        if (scheduledFutures == null) {
            // spring版本不同选用不同字段scheduledFutures
            taskRegistrar.getScheduledTasks();
            scheduledFutures = (Set<ScheduledFuture<?>>) SmartBeanUtil.getFieldValue("scheduledTasks", taskRegistrar);
        }
        return scheduledFutures;
    }

    /**
     * 添加任务
     */
    public void addTask(String taskId, TriggerTask triggerTask) {
        if (taskFutures.containsKey(taskId)) {
            throw new SchedulingException("the taskId[" + taskId + "] was added.");
        }
        TaskScheduler scheduler = taskRegistrar.getScheduler();
        ScheduledFuture<?> future = scheduler.schedule(triggerTask.getRunnable(), triggerTask.getTrigger());
        getScheduledFutures().add(future);
        taskFutures.put(taskId, future);
    }

    /**
     * 取消任务
     */
    public void cancelTask(String taskId) {
        ScheduledFuture<?> future = taskFutures.get(taskId);
        if (future != null) {
            future.cancel(true);
        }
        taskFutures.remove(taskId);
        getScheduledFutures().remove(future);
    }

    /**
     * 重置任务
     */
    public void resetTask(String taskId, TriggerTask triggerTask) {
        cancelTask(taskId);
        addTask(taskId, triggerTask);
    }

    /**
     * 任务编号
     */
    public Set<String> taskIds() {
        return taskFutures.keySet();
    }

    /**
     * 是否存在任务
     */
    public boolean hasTask(String taskId) {
        return this.taskFutures.containsKey(taskId);
    }

    /**
     * 任务调度是否已经初始化完成
     */
    public boolean init() {
        return this.taskRegistrar != null && this.taskRegistrar.getScheduler() != null;
    }

    public void destroy() {
        List<Task> taskList = new ArrayList<>();
        taskList.addAll(taskRegistrar.getCronTaskList());
        taskList.addAll(taskRegistrar.getTriggerTaskList());
        taskList.addAll(taskRegistrar.getFixedDelayTaskList());
        taskList.addAll(taskRegistrar.getFixedRateTaskList());

        List<String> taskName = taskList.stream().map(Task::toString).collect(Collectors.toList());

        taskRegistrar.destroy();

        log.warn("已结束 Bot Service 定时任务:\n{}", StringUtils.join(taskName, '\n'));
    }

}
