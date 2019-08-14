package alexander.ivanov.cache.cache.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.ref.SoftReference;

public class SoftReferenceCacheElementImpl extends SoftReference<CacheElementImpl<?,?>> {
    private static final Logger logger = LoggerFactory.getLogger(SoftReferenceCacheElementImpl.class);

    public SoftReferenceCacheElementImpl(Object key, Object value) {
        super(new CacheElementImpl<>(key, value));
    }

    @Override
    public String toString() {
        return "Reference:" + this.get();
    }
}
