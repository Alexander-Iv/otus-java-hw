package alexander.ivanov.webserver.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.Principal;
import java.util.Optional;

public class Index implements Filter {
    private static final Logger logger = LoggerFactory.getLogger(Index.class);
    private ServletContext context;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.info("!filterConfig = " + filterConfig);
        context = filterConfig.getServletContext();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = ((HttpServletRequest) request);
        //HttpSession session = req.getSession(false);
        String pathInfo = Optional.ofNullable(req.getPathInfo()).orElse("");
        String servletPath = Optional.ofNullable(req.getServletPath()).orElse("");
        String reqUri = Optional.ofNullable(req.getRequestURI()).orElse("");
        StringBuffer reqUrl = Optional.ofNullable(req.getRequestURL()).orElse(new StringBuffer());
        Principal userPrincipal = req.getUserPrincipal();

        logger.info("pathInfo = " + pathInfo);
        logger.info("servletPath = " + servletPath);
        logger.info("reqUri = " + reqUri);
        logger.info("reqUrl = " + reqUrl);
        logger.info("userPrincipal = " + userPrincipal);
        logger.info("response.isCommitted() = " + response.isCommitted());

        /*if (userPrincipal == null && (!reqUri.contains("/registration") || !reqUri.contains("/login"))) {
            ((HttpServletResponse) response).sendRedirect("/login");
        }*/

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}
