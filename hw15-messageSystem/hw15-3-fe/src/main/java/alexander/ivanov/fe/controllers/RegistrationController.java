package alexander.ivanov.fe.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.annotation.SessionScope;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@SessionScope
@RequestMapping("/registration")
public class RegistrationController {
    private static final Logger logger = LoggerFactory.getLogger(RegistrationController.class);

    @GetMapping
    public void get() {
        logger.debug("RegistrationController.get");
    }

    @PostMapping
    public String post(HttpSession session, HttpServletRequest request) {
        logger.debug("RegistrationController.post");
        logger.debug("session = {}", session);

        logger.info("post session.getAttribute(\"name\") = {}", session.getAttribute("name"));
        logger.info("request.getSession(false).getAttribute(\"name\") = {}", request.getSession(false).getAttribute("name"));
        return "home";
    }
}
