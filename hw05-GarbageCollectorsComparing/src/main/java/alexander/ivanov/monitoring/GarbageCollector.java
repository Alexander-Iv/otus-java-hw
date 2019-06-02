package alexander.ivanov.monitoring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GarbageCollector implements GarbageCollectorMBean, Runnable {
    private static final Logger logger = LoggerFactory.getLogger(GarbageCollector.class);

    private String type;
    private int count;
    private long duration;
    private long totalDuration;

    public GarbageCollector(GC_TYPES gcTypes) {
        this.type = gcTypes.name();
        count = 0;
        duration = 0;
        totalDuration = 0;
    }

    @Override
    public void run() {
        while (Thread.currentThread().isAlive()) {
            printAll();
            try {
                if (!Thread.currentThread().isAlive()) {
                    logger.info("continue");
                    continue;
                }
                Thread.sleep(60*1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
        printAll();
    }

    public void printAll() {
        totalDuration += duration;
        logger.info("type = " + type +
                ", count = " + count +
                ", totalDuration " + totalDuration + " ms" +
                ", duration = " + duration + " ms");
        duration = 0;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public long getDuration() {
        return duration;
    }

    @Override
    public void setDuration(long duration) {
        this.duration = duration;
    }
}