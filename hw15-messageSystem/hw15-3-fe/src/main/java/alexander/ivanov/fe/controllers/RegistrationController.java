package alexander.ivanov.fe.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/registration")
public class RegistrationController {
    private static final Logger logger = LoggerFactory.getLogger(RegistrationController.class);
    //private final UserService userService;
    private final HttpSession session;
    //private final FeService feService;

    public RegistrationController(/*FeService feService, */HttpSession session) {
        this.session = session;
        //this.feService = feService;
    }

    @GetMapping
    public void get() {
        logger.debug("RegistrationController.get");
    }

    @PostMapping
    public String post(Model model, String userName, String userPassword) {
        logger.debug("RegistrationController.post");
        logger.debug("session = {}", session);
        //User newUser = new User(userName, userPassword);
        //feService.registration(userName, userPassword);
        //userService.add(newUser);
        //model.addAttribute("users", userService.findAll());
        if(session.getAttribute("name") == null) {
            return "auth/login";
        }
        model.addAttribute("userName", session.getAttribute("name"));
        return "home";
    }
}
