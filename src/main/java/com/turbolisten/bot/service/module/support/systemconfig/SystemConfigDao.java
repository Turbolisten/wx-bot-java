package com.turbolisten.bot.service.module.support.systemconfig;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.turbolisten.bot.service.module.support.systemconfig.domain.SystemConfigEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

/**
 * 系统参数配置 t_system_config Dao
 *
 * @author listen
 * @date 2017-12-23 14:25
 */
@Component
@Mapper
public interface SystemConfigDao extends BaseMapper<SystemConfigEntity> {

    /**
     * 根据key查询获取数据
     *
     * @param key
     * @return
     */
    SystemConfigEntity getByKey(@Param("key") String key);
}
