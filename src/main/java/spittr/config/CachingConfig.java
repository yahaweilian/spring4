package spittr.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching//启用缓存
public class CachingConfig {

	@Bean
	public CacheManager cacheManager(){ //声明缓存管理器
		return new ConcurrentMapCacheManager();
	}
}
