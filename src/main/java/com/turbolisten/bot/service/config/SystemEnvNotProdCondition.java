package com.turbolisten.bot.service.config;

import com.turbolisten.bot.service.common.constant.SystemEnvironmentEnum;
import com.turbolisten.bot.service.util.SmartBaseEnumUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * 是否非生产环境
 *
 * @author listen
 * @date 2019/08/27 08:56
 */
@Configuration
public class SystemEnvNotProdCondition implements Condition {

    @Value("${spring.profiles.active}")
    private String systemEnvironment;

    @Override
    public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {
        String property = conditionContext.getEnvironment().getProperty("spring.profiles.active");
        return StringUtils.isNotBlank(property) && !SystemEnvironmentEnum.PROD.equalsValue(property);
    }

    @Bean
    public SystemEnvironmentEnum initEnvironment() {
        return SmartBaseEnumUtil.getEnumByValue(systemEnvironment, SystemEnvironmentEnum.class);
    }
}
