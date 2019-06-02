package alexander.ivanov;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MemoryLeakEmulator {
    private static final Logger logger = LoggerFactory.getLogger(MemoryLeakEmulator.class);

    private List<Integer> array = new ArrayList<>();
    private final int BATCH = 500_000;
    private final int BATCH_COUNT = 10_000;
    private final long ITERATION_LIMIT = BATCH*BATCH_COUNT;
    private final int WAIT_PERIOD = 100;

    public static void run() {
        new MemoryLeakEmulator().fillArray();
    }

    private void fillArray() {
        for (int i = 0; i < ITERATION_LIMIT; i += BATCH) {
            array.addAll(IntStream.range(Integer.valueOf(i), Integer.valueOf(i+BATCH)).boxed().collect(Collectors.toList()));

            removeSomeValuesAndWait();
        }
    }

    private void removeSomeValuesAndWait() {
        array.removeIf(integer -> (integer%2 == 0));
        try {
            Thread.sleep(WAIT_PERIOD);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}