package site.teamo.biu.net.server.web.config;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalNotification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * @author 爱做梦的锤子
 * @create 2021/3/5 14:27
 */
@Configuration
@Slf4j
public class CacheConfig {


    @Bean(name = "userCache")
    public Cache<String, String> userCache() {
        return CacheBuilder.newBuilder()
                .maximumSize(2)
                .expireAfterAccess(10, TimeUnit.SECONDS)
                .expireAfterWrite(10, TimeUnit.SECONDS)
                .removalListener(
                        (RemovalNotification<String, String> notification) ->
                                log.info("User cache {}:{} has expired"))
                .recordStats()
                .build();
    }
}
