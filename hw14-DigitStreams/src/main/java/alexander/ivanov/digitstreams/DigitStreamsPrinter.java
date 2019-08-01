package alexander.ivanov.digitstreams;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

public class DigitStreamsPrinter {
    private static final Logger logger = LoggerFactory.getLogger(DigitStreamsPrinter.class);
    private AtomicInteger digit;
    private int start;
    private int end;

    public DigitStreamsPrinter() {
        this(0, 11);
    }

    public DigitStreamsPrinter(int start, int end) {
        this.start = start;
        this.end = end;
        this.digit = new AtomicInteger(start);
    }

    public void demoRun() {
        Thread digitStream1 = new Thread(this::printDigitsInOrderAndInReverseOrder, "1");
        Thread digitStream2 = new Thread(this::printDigitsInOrderAndInReverseOrder, "2");
        //Thread digitStream3 = new Thread(this::printDigitsInOrderAndInReverseOrder, "3");
        digitStream1.start();
        digitStream2.start();
        //digitStream3.start();

        try {
            digitStream1.join();
            digitStream2.join();
            //digitStream3.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void printDigitsInOrderAndInReverseOrder() {
        StringBuffer res = new StringBuffer();
        for (int i = start; i < end; i++) {
            while (!digit.compareAndSet(digit.get(), i)) {

            }
            print(digit.get(), res);
        }
        for (int i = end; i > start; i--) {
            while (!digit.compareAndSet(digit.get(), i)) {

            }
            print(digit.get(), res);
        }
    }

    private void print(int value, StringBuffer text) {
        text.append(value).append(" ");
        logger.info("{}", text.toString().replaceFirst("[\\d+]", String.format("[%s]", Thread.currentThread().getName())));
        waiting(500);
    }

    private void waiting(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
