package alexander.ivanov.cache.cache;

import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.LongStream;

public class CacheDemo {
    private static final Logger logger = LoggerFactory.getLogger(CacheDemo.class);

    public static void main(String[] args) {
        CacheManager cacheManager = CacheManagerBuilder.newCacheManagerBuilder()
                .withCache("myCache", CacheConfigurationBuilder.newCacheConfigurationBuilder(Long.class, String.class, ResourcePoolsBuilder.heap(100)).build())
                .build(true);
        logger.info("cacheManager = {}", cacheManager);

        Cache<Long, String> myCache1 = cacheManager.getCache("myCache", Long.class, String.class);
        logger.info("myCache1 = {}", myCache1);

        Cache<Long, String> myCache2 = cacheManager.createCache("myCache2", CacheConfigurationBuilder.newCacheConfigurationBuilder(Long.class, String.class, ResourcePoolsBuilder.heap(100)).build());
        logger.info("myCache2 = {}", myCache2);

        LongStream.range(0L, 1001L).forEach(value -> {
            String val = String.valueOf(Math.random());
            logger.info("key = {}, value = {}", value, val);
            myCache1.put(value, val);
        });

        cacheManager.close();
    }
}
