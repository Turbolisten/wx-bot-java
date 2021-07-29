package com.turbolisten.bot.service.module.support.systemconfig;

import com.alibaba.fastjson.JSON;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.turbolisten.bot.service.module.support.systemconfig.domain.SystemConfigEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * 系统配置业务类
 *
 * @author GHQ
 * @date 2017-12-23 15:09
 */
@Slf4j
@Service
public class SystemConfigService {

    /**
     * 系统配置缓存
     */
    private Cache<String, SystemConfigEntity> cache = CacheBuilder.newBuilder()
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .build();

    @Autowired
    private SystemConfigDao systemConfigDao;

    private SystemConfigEntity getConfig(String key) {
        SystemConfigEntity configEntity = null;
        try {
            configEntity = cache.get(key, () -> systemConfigDao.getByKey(key));
        } catch (ExecutionException e) {
            e.printStackTrace();
            log.error("query config error: ", e);
        }
        return configEntity;
    }

    /**
     * 根据参数key获得一条数据 并转换为 对象
     *
     * @param configKey
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T getByKey(String configKey, Class<T> clazz) {
        SystemConfigEntity configEntity = this.getConfig(configKey);
        if (null == configEntity) {
            return null;
        }
        String configValue = configEntity.getConfigValue();
        if (StringUtils.isEmpty(configValue)) {
            return null;
        }
        return JSON.parseObject(configValue, clazz);
    }
}
