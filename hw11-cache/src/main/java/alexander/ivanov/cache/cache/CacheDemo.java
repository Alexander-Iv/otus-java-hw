package alexander.ivanov.cache.cache;

import alexander.ivanov.cache.cache.impl.CacheImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CacheDemo {
    private static final Logger logger = LoggerFactory.getLogger(CacheDemo.class);
    private static final int CACHE_SIZE = 100_000;

    public static void main(String[] args) throws InterruptedException {
        Cache softCache = new CacheImpl<>(CACHE_SIZE, 0, 0, true);

        int batch = 100;
        int counter = 0;
        int from = 0, to = 0;
        for (int i = 0; i < 1000; i++) {
            if (i == 0) {
                from = i * ++counter;
            } else {
                from = to;
            }
            to = from + batch;
            //logger.info("i = {}, from = {}, to = {}", i, from, to);
            addBigObjects(softCache, from, to);
            roundElements(softCache, 0, to);

            System.out.println("CacheImpl hits: " + String.format("%,d", softCache.getHitCount()));
            System.out.println("CacheImpl misses: " + String.format("%,d", softCache.getMissCount()));
            Thread.sleep(250);
        }
        softCache.dispose();
    }

    private static void addBigObjects(alexander.ivanov.cache.cache.Cache cache, int start, int end) {
        for (int i = start; i < end; i++) {
            cache.put(i, new BigObject());
        }
    }

    private static void roundElements(alexander.ivanov.cache.cache.Cache cache, int start, int end) {
        for (int i = start; i < end; i++) {
            cache.get(i);
        }
    }

    static class BigObject {
        final byte[] array = new byte[1024 * 1024];

        public byte[] getArray() {
            return array;
        }
    }
}
