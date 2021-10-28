package ua.training.top.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import ua.training.top.AuthorizedUser;
import ua.training.top.SecurityUtil;
import ua.training.top.util.ValidationUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @ExceptionHandler(Exception.class)
    public ModelAndView defaultErrorHandler(HttpServletRequest req, Exception e) throws Exception {
        log.error("Exception at request {} - {}", req.getRequestURL(), e);
        Throwable rootCause = ValidationUtil.getRootCause(e);

        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        ModelAndView mav = new ModelAndView("exception",
                Map.of("exception", rootCause.getMessage(), "message", rootCause.getClass().getSimpleName(), "status", httpStatus));
        mav.setStatus(httpStatus);

        // Interceptor is not invoked, put user
        AuthorizedUser authorizedUser = SecurityUtil.safeGet();
        if (authorizedUser != null) {
            mav.addObject("user", authorizedUser.getUser());
        }
        return mav;
    }
}
