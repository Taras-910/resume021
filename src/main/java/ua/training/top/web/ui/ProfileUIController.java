package ua.training.top.web.ui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.support.SessionStatus;
import springfox.documentation.annotations.ApiIgnore;
import ua.training.top.SecurityUtil;
import ua.training.top.model.User;
import ua.training.top.service.UserService;
import javax.validation.Valid;
import static ua.training.top.util.MessagesUtil.*;

@ApiIgnore
@Controller
@RequestMapping("/profile")
public class ProfileUIController {
public static final Logger log = LoggerFactory.getLogger(ProfileUIController.class);
    @Autowired
    UserService service;

    @GetMapping
    public String profile() {
        return "profile";
    }

    @PostMapping
    public String updateProfile(@Valid User user, BindingResult result, SessionStatus status) {
        log.info("updateProfile user {}", user);
        if (result.hasErrors()) {
            return "profile";
        }
        try {
            service.update(user, SecurityUtil.authUserId());
            SecurityUtil.get().update(user);
            status.setComplete();
            return "resumes";
        } catch (DataIntegrityViolationException ex) {
            result.rejectValue("email", null, user_exist_error);
            return "profile";
        }
    }

    @GetMapping("/register")
    public String register(ModelMap model) {
        model.addAttribute("user", new User());
        model.addAttribute("register", true);
        return "profile";
    }

    @PostMapping("/register")
    public String saveRegister(@Valid User user, BindingResult result, SessionStatus status, ModelMap model) {
        if (result.hasErrors()) {
            model.addAttribute("register", true);
            return "profile";
        } else {
            if(!user.getEmail().matches(email_matcher)){
                result.rejectValue("email", null, email_error);
                model.addAttribute("register", true);
                return "profile";
            }
            try {
                service.create(user);
                status.setComplete();
                return "redirect:/login" + invite_sign_in + user.getEmail();
            } catch (DataIntegrityViolationException e) {
                result.rejectValue("email", null, user_exist_error);
                model.addAttribute("register", true);
                return "profile";
            }
        }
    }
}
