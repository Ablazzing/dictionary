package org.javaacademy.dictionary.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static java.util.concurrent.TimeUnit.SECONDS;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public Caffeine caffeine() {
        return Caffeine.newBuilder().expireAfterAccess(15, SECONDS);
    }
}
