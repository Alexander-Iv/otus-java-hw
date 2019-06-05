package alexander.ivanov.monitoring;

public interface GarbageCollectorMBean {
    /*String type();
    int count();
    long duration();*/
    String getType();
    int getCount();
    void setCount(int count);
    long getDuration();
    void setDuration(long duration);
}
