package com.beside.config;

import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
@Log4j2
public class CacheConfig {
    @Bean
    public CacheManager cacheManager() {
        log.debug("[+] CacheConfig Start !!! ");
        // "codeCacheInfo"라는 캐시를 관리합니다.
        return new ConcurrentMapCacheManager("codeCache");
    }
}
