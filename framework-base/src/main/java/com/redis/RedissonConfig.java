package com.redis;

import org.redisson.config.Config;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Classname RedissonConfig
 * @Description TODO
 * @Date 2020/1/7 17:58
 * @Created by 125937
 */
@ConfigurationProperties(prefix = "spring.redisson")
@Component
public class RedissonConfig extends Config {
}
