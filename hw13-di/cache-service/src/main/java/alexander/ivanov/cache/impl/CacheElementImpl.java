package alexander.ivanov.cache.impl;

import alexander.ivanov.cache.CacheElement;

public class CacheElementImpl<K, V> implements CacheElement<K, V> {
    private final K key;
    private final V value;
    private final long creationTime;
    private long lastAccessTime;

    public CacheElementImpl(K key, V value) {
        this(key, value, System.currentTimeMillis(), System.currentTimeMillis());
    }

    private CacheElementImpl(K key, V value, long creationTime, long lastAccessTime) {
        this.key = key;
        this.value = value;
        this.creationTime = creationTime;
        this.lastAccessTime = lastAccessTime;
    }

    @Override
    public K getKey() {
        return key;
    }

    @Override
    public V getValue() {
        return value;
    }

    @Override
    public long getCreationTime() {
        return creationTime;
    }

    @Override
    public long getLastAccessTime() {
        return lastAccessTime;
    }

    @Override
    public void setAccessed() {
        this.lastAccessTime = System.currentTimeMillis();
    }

    @Override
    public String toString() {
        return "{key=" + key + ", value=" + value + " creationTime=" + creationTime + " lastAccessTime=" + lastAccessTime + "}";
    }
}
