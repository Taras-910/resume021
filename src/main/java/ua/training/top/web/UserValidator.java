package ua.training.top.web;

import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import ua.training.top.model.User;
import ua.training.top.repository.UserRepository;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

import static ua.training.top.web.ExceptionInfoHandler.EXCEPTION_DUPLICATE_EMAIL;

@Component
public class UserValidator implements org.springframework.validation.Validator {

    private final UserRepository repository;
    private final HttpServletRequest request;

    public UserValidator(UserRepository repository, @Nullable HttpServletRequest request) {
        this.repository = repository;
        this.request = request;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;
        if (StringUtils.hasText(user.getEmail())) {
            User userDB = Optional.ofNullable(repository.getByEmail(user.getEmail().toLowerCase())).orElse(new User());
            if (userDB.getEmail() != null && !userDB.getId().equals(user.getId())) {
                String requestURI = request.getRequestURI();
                if (requestURI.contains("/profile")) {
                    errors.rejectValue("email", EXCEPTION_DUPLICATE_EMAIL);
                }
            }
        }
    }
}
