package com.lec.clock.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * Redisson配置类
 */
@Configuration
public class RedissonConfig {
    @Resource
    private RedisProperties redisProperties;

    @Bean
    public RedissonClient redissonClient() {
        // 配置类
        Config config = new Config();
        // 添加redis地址，这里添加了单点的地址，也可以使用config.useClusterServers()添加集群地址
        // 不推荐使用明文，所以我自定义了个配置文件
        config.useSingleServer().setAddress("redis://"+redisProperties.getHost()+":6379").setPassword(redisProperties.getPassword());
        // 创建RedissonClient客户端对象
        return Redisson.create(config);
    }
}