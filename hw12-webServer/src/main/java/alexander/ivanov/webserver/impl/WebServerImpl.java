package alexander.ivanov.webserver.impl;

import alexander.ivanov.webserver.WebServer;
import alexander.ivanov.webserver.util.Loader;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class WebServerImpl implements WebServer {
    private Server server;

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    private class JettyServerUtil {
        public Server createServer(int port) {
            ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
            new Loader(context).executeOnDir("servlets/", new Loader.AddServlet());
            new Loader(context).executeOnDir("filters/", new Loader.AddFilter());
            /*context.AddServlet(new ServletHolder(new PublicInfo()), "/publicInfo");
            context.AddServlet(new ServletHolder(new PrivateInfo()), "/privateInfo");
            context.AddServlet(new ServletHolder(new Data()), "/data/*");

            context.addFilter(new FilterHolder(new SimpleFilter()), "/*", null);*/

            Server server = new Server(port);
            server.setHandler(new HandlerList(context));

            HandlerList handlers = new HandlerList();
            handlers.setHandlers(new Handler[]{/*createResourceHandler(), createSecurityHandler(context)*/});
            server.setHandler(handlers);
            return server;
        }
    }
}
