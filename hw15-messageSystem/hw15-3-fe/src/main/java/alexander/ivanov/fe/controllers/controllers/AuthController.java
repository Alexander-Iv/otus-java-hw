package alexander.ivanov.fe.controllers.controllers;

import alexander.ivanov.dbservice.database.hibernate.model.User;
import alexander.ivanov.fe.services.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Collection;

@Controller
@RequestMapping("/auth")
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private final HttpSession session;
    private final UserService userService;

    public AuthController(HttpSession session, UserService userService) {
        this.session = session;
        this.userService = userService;
    }

    @GetMapping("/login")
    public void getLogin() {
        logger.debug("AuthController.getLogin");
    }

    @PostMapping("/login")
    public String postLogin(Model model, String userName, String userPassword) throws IOException {
        User newUser = new User(userName, userPassword);
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
        }
        return "home";
    }

    @GetMapping("/logout")
    public String logout(Model model) {
        logger.debug("AuthController.logout");
        session.setAttribute("name", "");
        model.addAttribute("userName", "");
        return "auth/login";
    }
}
