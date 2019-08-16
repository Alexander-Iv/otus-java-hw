package alexander.ivanov.cache;

import alexander.ivanov.cache.impl.CacheImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CacheDemo {
    private static final Logger logger = LoggerFactory.getLogger(CacheDemo.class);

    public static void main(String[] args) throws InterruptedException {
        logger.info("DEMO:");
        demo();
        logger.info("CACHE_SORTING_DEMO:");
        cacheSortingDemo();
    }

    public static void demo() throws InterruptedException {
        final int CACHE_SIZE = 200;
        Cache softCache = new CacheImpl<>(CACHE_SIZE, 0, 0, true);
        addBigObjects(softCache, 0, CACHE_SIZE);
        System.out.println("CacheImpl hits: " + String.format("%,d", softCache.getHitCount()));
        System.out.println("CacheImpl misses: " + String.format("%,d", softCache.getMissCount()));

        roundElements(softCache, 0, CACHE_SIZE);
        System.out.println("CacheImpl hits: " + String.format("%,d", softCache.getHitCount()));
        System.out.println("CacheImpl misses: " + String.format("%,d", softCache.getMissCount()));

        softCache.dispose();
    }

    static class BigObject {
        final byte[] array = new byte[1024 * 1024];

        public byte[] getArray() {
            return array;
        }
    }

    public static void cacheSortingDemo() {
        final int CACHE_SIZE = 5;
        Cache softCache = new CacheImpl<>(CACHE_SIZE, 0, 0, true);
        addBigObjects(softCache, 0, 5);
        logger.debug("Default cache content:");
        logger.debug("softCache = {}", softCache);

        softCache.put(0, new BigObject());
        logger.debug("After adding new element by index 0:");
        logger.debug("softCache = {}", softCache);

        softCache.get(1);
        logger.debug("After getting element by index 1:");
        logger.debug("softCache = {}", softCache);

        softCache.get(0);
        logger.debug("After getting element by index 0:");
        logger.debug("softCache = {}", softCache);

        softCache.put(5, new BigObject());
        softCache.put(6, new BigObject());
        logger.debug("After adding element by index 5 and 6:");
        logger.debug("softCache = {}", softCache);
        //roundElements(softCache, 0, 5);

        softCache.get(10);
        logger.debug("After getting not exists element by index 0:");
        logger.debug("softCache = {}", softCache);

        softCache.get(6);
        logger.debug("After getting element by index 6:");
        logger.debug("softCache = {}", softCache);

        System.out.println("CacheImpl hits: " + String.format("%,d", softCache.getHitCount()));
        System.out.println("CacheImpl misses: " + String.format("%,d", softCache.getMissCount()));

        softCache.dispose();
    }

    private static void addBigObjects(Cache cache, int start, int end) {
        for (int i = start; i < end; i++) {
            cache.put(i, new BigObject());
        }
    }

    private static void roundElements(Cache cache, int start, int end) {
        for (int i = start; i < end; i++) {
            cache.get(i);
        }
    }
}
