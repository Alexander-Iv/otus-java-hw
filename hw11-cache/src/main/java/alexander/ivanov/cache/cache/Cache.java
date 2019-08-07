package alexander.ivanov.cache.cache;

public interface Cache<K, V> {
    void put(K key, V value);
    void remove(K key);
    <R> R get(K key);
    int getHitCount();
    int getMissCount();
    void dispose();
}