package alexander.ivanov.webserver.util.appender;

public interface Appender<T> {
    void append(T source, String path);
}
