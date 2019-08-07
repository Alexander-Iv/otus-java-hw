package alexander.ivanov.cache.cache.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.ref.SoftReference;

public class SoftReferenceCacheElementImpl<K, V> extends CacheElementImpl<K, SoftReference<V>> {
    private static final Logger logger = LoggerFactory.getLogger(SoftReferenceCacheElementImpl.class);

    public SoftReferenceCacheElementImpl(K key, V value) {
        super(key, new SoftReference<>(value));
        //logger.info("value = {}", super.getValue());
    }
}
