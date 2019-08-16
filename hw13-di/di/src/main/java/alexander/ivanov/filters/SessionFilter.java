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

        Optional<HttpSession> session = Optional.ofNullable(req.getSession(false));
        session.ifPresentOrElse(httpSession -> {
            logger.debug("httpSession = {}", httpSession);
            try {
                chain.doFilter(request, response);
            } catch (IOException | ServletException e) {
                e.printStackTrace();
            }
        }, () -> {
                if (!req.getServletPath().contains("/auth/login")) {
                    try {
                        logger.debug("session = {}, req.getServletPath() = {}", session, req.getServletPath());
                        RequestDispatcher dispatcher = req.getRequestDispatcher("auth/login");
                        //dispatcher.include(request, response);
                        //dispatcher.forward(request, response);
                        resp.sendRedirect(req.getContextPath() + "/auth/login");
                    } catch (IOException /*| ServletException*/ e) {
                        e.printStackTrace();
                    }
                }
        });
    }
}
