package alexander.ivanov.examples;

import alexander.ivanov.annotations.Log;

public class SimpleLoggingImpl implements SimpleLogging {
    @Log
    @Override
    public void calculation(int param) {

    }

    @Log
    @Override
    public void add(int a, int b) {

    }

    @Log
    @Override
    public int pow(int a, int b) {
        return a*b;
    }
}
