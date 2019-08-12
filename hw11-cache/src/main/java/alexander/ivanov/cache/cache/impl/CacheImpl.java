package alexander.ivanov.cache.cache.impl;

import alexander.ivanov.cache.cache.Cache;
import alexander.ivanov.cache.cache.CacheElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.ref.Reference;
import java.util.*;
import java.util.function.Function;

public class CacheImpl<K, V> implements Cache<K, V> {
    private static final Logger logger = LoggerFactory.getLogger(CacheImpl.class);
    private static final int TIME_THRESHOLD_MS = 0;

    private final int maxElements;
    private final long lifeTimeMs;
    private final long idleTimeMs;
    private final boolean isEternal;

    private final Map<K, Reference<CacheElement<K, V>>> elements;
    private final Timer timer = new Timer();

    private int hit = 0;
    private int miss = 0;

    public CacheImpl(int maxElements, long lifeTimeMs, long idleTimeMs, boolean isEternal) {
        this.elements = new LinkedHashMap<>(maxElements, 0.75F, true);
        this.maxElements = maxElements;
        this.lifeTimeMs = lifeTimeMs > 0 ? lifeTimeMs : 0;
        this.idleTimeMs = idleTimeMs > 0 ? idleTimeMs : 0;
        this.isEternal = lifeTimeMs == 0 && idleTimeMs == 0 || isEternal;
    }

    @Override
    public void put(K key, V value) {
        if (elements.size() > maxElements) {
            K firstKey = elements.keySet().iterator().next();
            elements.remove(firstKey);
        } else {
            Reference element = new SoftReferenceCacheElementImpl(key, value);
            elements.put(key, element);

            if (!isEternal) {
                if (lifeTimeMs != 0) {
                    TimerTask lifeTimerTask = getTimerTask(key, lifeElement -> lifeElement.getCreationTime() + lifeTimeMs);
                    logger.info("lifeTimerTask = {}", lifeTimerTask);
                    timer.schedule(lifeTimerTask, lifeTimeMs);
                }
                if (idleTimeMs != 0) {
                    TimerTask idleTimerTask = getTimerTask(key, idleElement -> idleElement.getLastAccessTime() + idleTimeMs);
                    timer.schedule(idleTimerTask, idleTimeMs, idleTimeMs);
                }
            }
        }
    }

    @Override
    public void remove(K key) {
        if (key != null) {
            elements.remove(key);
        }
    }

    @Override
    public V get(K key) {
        try {
            return Optional.ofNullable(key)
                    .map(k -> Optional.ofNullable(elements.get(key)))
                    .map(cacheElementReference -> {
                        Optional<CacheElement<K, V>> element = Optional.ofNullable(cacheElementReference.get().get());
                        if(element.isPresent()) {
                            element.get().setAccessed();
                            hit++;
                        } else {
                            miss++;
                        }
                        return element.get().getValue();
                    }).get();
        } catch (NoSuchElementException e) {
            logger.warn("Element {} not found", key);
            miss++;
        }
        return null;
    }

    @Override
    public int getHitCount() {
        return hit;
    }

    @Override
    public int getMissCount() {
        return miss;
    }

    @Override
    public void dispose() {
        timer.cancel();
    }

    @Override
    public String toString() {
        StringBuffer contentElements = new StringBuffer();
        elements.forEach((k, kSoftReferenceCacheElement) -> {
            contentElements.append("\n").append("key=").append(k).append("; ").append("value=").append(kSoftReferenceCacheElement);
        });
        return "CacheImpl{" +
                "elements=" + contentElements +
                '}' + "\n";
    }

    private TimerTask getTimerTask(final K key, Function<CacheElement<K, V>, Long> timeFunction) {
        return new TimerTask() {
            @Override
            public void run() {
                CacheElement<K, V> element = elements.get(key).get();
                if (element == null || isT1BeforeT2(timeFunction.apply(element), System.currentTimeMillis())) {
                    elements.remove(key);
                    this.cancel();
                }
            }
        };
    }

    private boolean isT1BeforeT2(long t1, long t2) {
        return t1 < t2 + TIME_THRESHOLD_MS;
    }
}
