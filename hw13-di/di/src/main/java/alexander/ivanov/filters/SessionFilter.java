package alexander.ivanov.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Optional;

public class SessionFilter implements Filter {
    private static final Logger logger = LoggerFactory.getLogger(SessionFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest)request;
        HttpServletResponse resp = (HttpServletResponse)response;

        logger.debug("req = {}", req);
        logger.debug("resp = {}", resp);

        /*Optional<HttpSession> session = Optional.ofNullable(req.getSession(false));
        session.ifPresentOrElse(httpSession -> {
            logger.debug("httpSession = {}", httpSession);
            try {
                chain.doFilter(request, response);
            } catch (IOException | ServletException e) {
                e.printStackTrace();
            }
        }, () -> {
            logger.debug("session = {}, req.getServletPath() = {}", session, req.getServletPath());
                //if (!req.getServletPath().contains("/auth/login")) {
                    try {
                        RequestDispatcher dispatcher = req.getRequestDispatcher("auth/login");
                        //dispatcher.include(request, response);
                        //dispatcher.forward(request, response);
                        //resp.sendRedirect(req.getContextPath() + "/auth/login");
                        resp.sendRedirect("auth/login");
                    } catch (IOException *//*| ServletException*//* e) {
                        e.printStackTrace();
                    }
                //}
        });*/
        String authPath;
        HttpSession session = req.getSession(false);
        if (session == null) {
            logger.info("session = " + session);
            logger.debug("req.getContextPath() = {}, req.getServletPath() = {}, info = {}", req.getContextPath(), req.getServletPath(), req.getPathInfo());
            if (!req.getServletPath().contains("login")) {
                logger.info("START auth condition");
                authPath = req.getContextPath() + "/auth/login";
                logger.info("authPath {}", authPath);
                //resp.sendRedirect(authPath);
                resp.sendRedirect(authPath);
                //resp.sendRedirect();
                logger.info("END auth condition");
            } else {
                logger.info("else do doFilter()");
                chain.doFilter(req, resp);
            }
        } else {
            logger.info("authorized session = " + session);
            authPath = req.getContextPath()+ "/home";
            logger.info("authPath {}", authPath);
            resp.sendRedirect(authPath);
            chain.doFilter(req, resp);
        }
    }
}
