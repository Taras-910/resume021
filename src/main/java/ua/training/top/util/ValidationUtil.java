package ua.training.top.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import ua.training.top.model.AbstractBaseEntity;
import ua.training.top.model.User;
import ua.training.top.util.exception.ErrorType;
import ua.training.top.util.exception.IllegalRequestDataException;
import ua.training.top.util.exception.NotFoundException;

import javax.servlet.http.HttpServletRequest;
import static ua.training.top.util.MessagesUtil.*;

public class ValidationUtil {
    public static final Logger log = LoggerFactory.getLogger(ValidationUtil.class);

    private ValidationUtil() {
    }

    public static <T> T checkNotFoundWithId(T object, int id) {
        checkNotFoundWithId(object != null, id);
        return object;
    }

    public static void checkNotFoundWithId(boolean found, int id) {
        checkNotFound(found, "id=" + id);
    }

    public static <T> T checkNotFound(T object, String msg) {
        checkNotFound(object != null, msg);
        return object;
    }

    public static void checkNotFound(boolean found, String msg) {
        if (!found) {
            throw new NotFoundException("Not found entity with " + msg);
        }
    }

    public static void checkNew(AbstractBaseEntity entity) {
        if (!entity.isNew()) {
            throw new IllegalRequestDataException(entity + " must be new (id=null)");
        }
    }

    public static void assureIdConsistent(AbstractBaseEntity entity, int id) {
//      conservative when you reply, but accept liberally (http://stackoverflow.com/a/32728226/548473)
        if (entity.isNew()) {
            entity.setId(id);
        } else if (entity.id() != id) {
            throw new IllegalRequestDataException(entity + " must be with id=" + id);
        }
    }

    //  http://stackoverflow.com/a/28565320/548473
    public static Throwable getRootCause(Throwable t) {
        Throwable result = t;
        Throwable cause;

        while (null != (cause = result.getCause()) && (result != cause)) {
            result = cause;
        }
        return result;
    }

    public static String getMessage(Throwable e) {
        return e.getLocalizedMessage() != null ? e.getLocalizedMessage() : e.getClass().getName();
    }

    public static Throwable logAndGetRootCause(Logger log, HttpServletRequest req, Exception e, boolean logException, ErrorType errorType) {
        Throwable rootCause = ValidationUtil.getRootCause(e);
        if (logException) {
            log.error(error_request_url, errorType , req.getRequestURL(), rootCause);
        } else {
            log.warn(error_request_url, errorType, req.getRequestURL(), rootCause.toString());
        }
        return rootCause;
    }

    public static void checkValidEmail(String email) {
        if(!email.matches(email_matcher)) {
            throw new IllegalArgumentException(email_error);
        }
    }

    public static void checkValidUrl(String url) {
        if(!url.matches(url_matcher)) {
            throw new IllegalArgumentException(url_error);
        }
    }

    public static void checkExistThisEmail(User user) {
        if(user != null) {
            throw new DataIntegrityViolationException(user_exist_error + user.getEmail());
        }
    }

}
