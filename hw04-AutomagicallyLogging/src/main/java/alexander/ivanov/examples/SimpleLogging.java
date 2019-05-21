package alexander.ivanov.examples;

import alexander.ivanov.logging.Logging;

public interface SimpleLogging extends Logging {
    void calculation(int param);
    void add(int a, int b);
    void add(float a, float b);
    int pow(int a, int b);
}
