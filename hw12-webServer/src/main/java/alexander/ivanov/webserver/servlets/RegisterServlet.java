package alexander.ivanov.webserver.servlets;

import alexander.ivanov.webserver.models.hibernate.dao.UserDao;
import alexander.ivanov.webserver.models.hibernate.dao.impl.UserDaoImpl;
import alexander.ivanov.webserver.models.hibernate.model.User;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

public class RegisterServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(RegisterServlet.class);
    private UserDao userDao;

    @Override
    public void init() throws ServletException {
        SessionFactory sessionFactory = (SessionFactory) getServletContext().getAttribute("sessionFactory");
        userDao = new UserDaoImpl(sessionFactory);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userName = Optional.ofNullable(String.valueOf(req.getParameter("userName"))).orElse("");
        String userPassword = Optional.ofNullable(String.valueOf(req.getParameter("userPassword"))).orElse("");

        if (!userName.isEmpty() && !userPassword.isEmpty()) {
            User newUser = new User(userName, userPassword);
            logger.info("1 register.newUser = " + newUser);

            try {
                userDao.create(newUser);
                if(req.getSession(false) != null) {
                    resp.sendRedirect("/home");
                }
            } catch (Exception e) {
                logger.error("e.getMessage() = " + e.getMessage());
            }

            logger.info("2 register.newUser = " + newUser);
        } else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }

        resp.sendRedirect("/login");
    }
}
