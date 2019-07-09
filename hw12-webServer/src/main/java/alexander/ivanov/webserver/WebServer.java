package alexander.ivanov.webserver;

public interface WebServer {
    //<T> T createServer(int port);
    void start();
    void stop();
}
