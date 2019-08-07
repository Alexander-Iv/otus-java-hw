package alexander.ivanov.cache.cache.impl;

import alexander.ivanov.cache.cache.Cache;
import alexander.ivanov.cache.cache.CacheElement;

import java.lang.ref.SoftReference;
import java.util.*;
import java.util.function.Function;

public class CacheImpl<K, V> implements Cache<K, V> {
    private static final int TIME_THRESHOLD_MS = 0;

    private final int maxElements;
    private final long lifeTimeMs;
    private final long idleTimeMs;
    private final boolean isEternal;

    private final Map<K, CacheElement<K, SoftReference<V>>> elements/* = new LinkedHashMap<>()*/;
    private final Timer timer = new Timer();

    private int hit = 0;
    private int miss = 0;

    public CacheImpl(int maxElements, long lifeTimeMs, long idleTimeMs, boolean isEternal) {
        this.elements = new LinkedHashMap<>(maxElements);
        this.maxElements = maxElements;
        this.lifeTimeMs = lifeTimeMs > 0 ? lifeTimeMs : 0;
        this.idleTimeMs = idleTimeMs > 0 ? idleTimeMs : 0;
        this.isEternal = lifeTimeMs == 0 && idleTimeMs == 0 || isEternal;
    }

    @Override
    public void put(K key, V value) {
        if (elements.size() == maxElements) {
            K firstKey = elements.keySet().iterator().next();
            elements.remove(firstKey);
        }

        CacheElement<K, SoftReference<V>> element = new SoftReferenceCacheElementImpl<>(key, value);
        elements.put(key, element);

        if (!isEternal) {
            if (lifeTimeMs != 0) {
                TimerTask lifeTimerTask = getTimerTask(key, lifeElement -> lifeElement.getCreationTime() + lifeTimeMs);
                timer.schedule(lifeTimerTask, lifeTimeMs);
            }
            if (idleTimeMs != 0) {
                TimerTask idleTimerTask = getTimerTask(key, idleElement -> idleElement.getLastAccessTime() + idleTimeMs);
                timer.schedule(idleTimerTask, idleTimeMs, idleTimeMs);
            }
        }
    }

    @Override
    public void remove(K key) {
        CacheElement<K, SoftReference<V>> element = elements.get(key);
        if (element != null) {
            elements.remove(key);
        }
    }

    @Override
    public <R> R get(K key) {
        V value = null;
        CacheElement<K, SoftReference<V>> softElement = elements.get(key);
        if (softElement != null) {
            value = elements.get(key).getValue().get();
            if (Objects.isNull(value)) {
                miss++;
            } else {
                hit++;
            }
        } else {
            miss++;
        }
        return (R) value;
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

    private TimerTask getTimerTask(final K key, Function<CacheElement<K, SoftReference<V>>, Long> timeFunction) {
        return new TimerTask() {
            @Override
            public void run() {
                CacheElement<K, SoftReference<V>> element = elements.get(key);
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
