package alexander.ivanov.examples;

import alexander.ivanov.logging.Logging;

public interface SimpleLogging extends Logging {
    void calculation(int param);
    void add(int a, int b);
    int pow(int a, int b);
}
