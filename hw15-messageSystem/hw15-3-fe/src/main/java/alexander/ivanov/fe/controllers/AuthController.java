package alexander.ivanov.fe.controllers;

import alexander.ivanov.fe.util.JsonHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.annotation.SessionScope;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@SessionScope
@RequestMapping("/auth")
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @GetMapping("/login")
    public String getLogin() {
        return "auth/login";
    }

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void postLogin(HttpSession session, @RequestBody String body, HttpServletRequest request) {
        logger.info("AuthController.postLogin");
        logger.info("body = {}", body);
        session = request.getSession();
        session.setAttribute("name", JsonHelper.readAuthNameFromJson(body));
        logger.info("session.getAttribute(\"name\") = {}", session.getAttribute("name"));
        logger.info("request.getSession() = {}", session);
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        logger.debug("AuthController.logout");
        session.setAttribute("name", null);
        return "auth/login";
    }
}
