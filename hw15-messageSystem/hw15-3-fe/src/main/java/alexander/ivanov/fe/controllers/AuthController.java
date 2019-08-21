package alexander.ivanov.fe.controllers;

import alexander.ivanov.messageSystem.services.FeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.Collections;

@Controller
@RequestMapping("/auth")
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private final HttpSession session;
    //private final UserService userService;
    private final FeService feService;

    @Autowired
    public AuthController(HttpSession session, /*UserService userService*/FeService feService) {
        this.session = session;
        //this.userService = userService;
        this.feService = feService;
        logger.debug("feService = {}", feService);
    }

    @GetMapping("/login")
    public void getLogin() {
        logger.debug("AuthController.getLogin");
    }

    @PostMapping("/login")
    public String postLogin(Model model, String userName, String userPassword) {
        try {
            //feService.init();
            feService.auth(userName, userPassword);
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
