package com.richotaru.notificationapi.configuration;

import com.hazelcast.config.CacheSimpleConfig;
import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.ICacheManager;
import io.github.bucket4j.grid.GridBucketState;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.cache.Cache;

@Configuration
public class HazelcastConfiguration{

    @Bean
    Cache<String, GridBucketState> cache() {
        HazelcastInstance hazelcastInstance = Hazelcast.newHazelcastInstance(hazelCastConfig());
        ICacheManager cacheManager = hazelcastInstance.getCacheManager();
        return cacheManager.getCache("grid-bucket");
    }

    @Bean
    public Config hazelCastConfig() {
        Config config = new Config();
        config.setLiteMember(false);
        CacheSimpleConfig cacheConfig = new CacheSimpleConfig();
        cacheConfig.setName("grid-bucket");
        config.addCacheConfig(cacheConfig);
        return config;
    }

}
