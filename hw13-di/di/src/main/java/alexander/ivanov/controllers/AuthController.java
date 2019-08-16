package alexander.ivanov.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/auth")
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private final HttpSession session;

    public AuthController(HttpSession session) {
        this.session = session;
    }

    @GetMapping
    public void getSession() {
        logger.debug("AuthController.getSession");
        logger.debug("session = {}", session);
    }

    @PostMapping("${userKey}/${userValue}")
    public void newSessionAttribute(@PathVariable String userKey, @PathVariable String userValue) {
        logger.debug("AuthController.newSessionAttribute");
        session.setAttribute(userKey, userValue);
    }

    @GetMapping("/login")
    public String getLogin() {
        logger.debug("AuthController.getLogin");
        return "auth/login";
    }

    @PostMapping("/login")
    public String postLogin(String userName) {
        logger.debug("AuthController.postLogin");
        String res = String.format("/auth/%s/%s", "name", userName);
        logger.debug("res = {}", res);
        return res;
    }

    @PostMapping("/logout")
    public void logout() {
        logger.debug("AuthController.logout");
    }
}
