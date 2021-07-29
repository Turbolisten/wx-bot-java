package com.turbolisten.bot.service.module.support.systemconfig.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 系统配置参数 实体类
 *
 * @author GHQ
 * @date 2017-12-23 13:41
 */
@Data
@TableName("t_system_config")
public class SystemConfigEntity {

    private Long id;

    /**
     * 参数key
     */
    private String configKey;

    /**
     * 参数的值
     */
    private String configValue;

    /**
     * 参数名称
     */
    private String configName;

    /**
     * 参数类别
     */
    private String configGroup;

    /**
     * 是否使用0 是 1否
     */
    private Boolean isUsing;

    /**
     * 备注
     */
    private String remark;

    private LocalDateTime createTime;
}
