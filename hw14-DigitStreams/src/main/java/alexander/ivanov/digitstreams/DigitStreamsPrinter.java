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
        this(1, 11);
    }

    public DigitStreamsPrinter(int start, int end) {
        this.start = start;
        this.end = end;
        this.digit = new AtomicInteger(start);
    }

    public void demoRun() {
        CommonObject commonObject = new CommonObject();
        Thread digitStream1 = new Thread(() -> printDigitsInOrderAndInReverseOrder(commonObject), "1");
        Thread digitStream2 = new Thread(() -> printDigitsInOrderAndInReverseOrder(commonObject), "2");
        digitStream1.start();
        digitStream2.start();

        try {
            digitStream1.join();
            digitStream2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void printDigitsInOrderAndInReverseOrder(CommonObject commonObject) {
        for (int i = start; i < end; i++) {
            while (!digit.compareAndSet(digit.get(), i)) {

            }
            print(digit.get(), commonObject.getResultText());
        }
        for (int i = end; i >= start; i--) {
            while (!digit.compareAndSet(digit.get(), i)) {

            }
            print(digit.get(), commonObject.getResultText());
        }
    }

    private synchronized void print(int value, StringBuffer text) {
        try {
            wait(100);
            text.append(value).append(" ");
            logger.info("text = {}", text);
            notifyAll();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    class CommonObject {
        private StringBuffer resultText = new StringBuffer();

        public StringBuffer getResultText() {
            return resultText;
        }

        public void setResultText(StringBuffer resultText) {
            this.resultText = resultText;
        }
    }
}
