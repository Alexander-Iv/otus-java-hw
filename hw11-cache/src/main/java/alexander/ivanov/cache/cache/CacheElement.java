package alexander.ivanov.cache.cache;

public interface CacheElement<K, V> {
    K getKey();
    V getValue();
    long getCreationTime();
    long getLastAccessTime();
    void setAccessed();
}
