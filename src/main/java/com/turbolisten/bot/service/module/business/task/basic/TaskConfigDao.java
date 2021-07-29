package com.turbolisten.bot.service.module.business.task.basic;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * TaskConfig dao
 *
 * @author Turbolisten
 * @date 2021/7/11 16:30
 */
@Component
public interface TaskConfigDao extends BaseMapper<TaskConfigEntity> {

    /**
     * 查询任务列表
     *
     * @param enabledFlag 可以null
     * @return
     */
    List<TaskConfigEntity> query(Boolean enabledFlag);
}
