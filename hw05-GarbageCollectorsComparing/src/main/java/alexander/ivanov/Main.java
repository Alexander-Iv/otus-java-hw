package alexander.ivanov;

import alexander.ivanov.monitoring.Monitoring;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        Monitoring.run();
        MemoryLeakEmulator.run();
    }
}