package alexander.ivanov.fe.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Collections;

@Controller
@RequestMapping("/auth")
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private HttpSession session;

    @Autowired
    public AuthController(HttpSession session) {
        this.session = session;
    }

    @PostMapping("/login/{login}")
    public String auth(@PathVariable String login, HttpServletRequest request) {
        logger.debug("AuthController.getLogin");
        session = request.getSession();
        session.setAttribute("name", login);
        logger.info("session.getAttribute(\"name\") = {}", session.getAttribute("name"));
        logger.info("request.getSession() = {}", session);
        //session.setAttribute("name", login);
        return "home";
    }

    @GetMapping("/login")
    public String getLogin() {
        return "auth/login";
    }

    @PostMapping("/login")
    public void postLogin(Model model, String userName, String userPassword) {
        try {
            //feService.init();
            //messageSystem.createMessageFor(dbService, userName + ", " + userPassword);
            //feService.auth(userName, userPassword);
            model.addAttribute("users", Collections.emptyList());
        } catch (Exception e) {
            e.printStackTrace();
        }

        /*User newUser = new User(userName, userPassword);
        Collection<User> users = userService.findAll();
        if (users.contains(newUser)) {
            session.setAttribute("name", userName);
            model.addAttribute("userName", userName);
            model.addAttribute("users", users);
            model.addAttribute("message", "");
        } else {
            logger.info("user({}) not found", userName);
            model.addAttribute("message", "Incorrect user or password. Please try again");
            return "auth/login";
        }*/
        //return "home";
    }

    @GetMapping("/logout")
    public String logout(Model model) {
        logger.debug("AuthController.logout");
        session.setAttribute("name", "");
        model.addAttribute("userName", "");
        return "auth/login";
    }
}
