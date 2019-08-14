package alexander.ivanov.digitstreams;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DigitStreamsDemo {
    private static final Logger logger = LoggerFactory.getLogger(DigitStreamsDemo.class);

    public static void main(String[] args) {
        DigitStreamsPrinter digitStreamsPrinter = new DigitStreamsPrinter();
        digitStreamsPrinter.demoRun();
    }
}
