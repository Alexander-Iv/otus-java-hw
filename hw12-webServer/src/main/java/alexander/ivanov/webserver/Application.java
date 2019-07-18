package alexander.ivanov.webserver;

import alexander.ivanov.webserver.impl.WebServerImpl;

public class Application {
    public static void main(String[] args) {
        WebServer server = new WebServerImpl();
        server.start();
    }
}
