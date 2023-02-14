package ua.training.top.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.MessageSourceAccessor;
import ua.training.top.model.AbstractBaseEntity;
import ua.training.top.model.Resume;
import ua.training.top.to.ResumeTo;
import ua.training.top.util.exception.ErrorType;
import ua.training.top.util.exception.IllegalRequestDataException;
import ua.training.top.util.exception.NotFoundException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static ua.training.top.SecurityUtil.authUserId;
import static ua.training.top.util.InformUtil.*;
import static ua.training.top.util.UserUtil.asAdmin;
import static ua.training.top.util.aggregateUtil.data.CommonUtil.isEmpty;

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
            throw new NotFoundException(not_found + msg);
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
            log.error(error_request_url, errorType, req.getRequestURL(), rootCause);
        } else {
            log.warn(error_request_url, errorType, req.getRequestURL(), rootCause.toString());
        }
        return rootCause;
    }

    public static void checkNotFoundData(boolean found, Object id) {
        if (!found) {
            log.error(not_found + id);
        }
    }

    public static boolean isNotSimilar(Resume r, ResumeTo rTo) {
        return !r.getTitle().equals(rTo.getTitle()) ||
                !r.getName().equals(rTo.getName());
    }


    public static void isNullPointerException(ResumeTo resumeTo) {
        if (!checkNullDataResumeTo(resumeTo)) {
            throw new NullPointerException(must_not_null + resumeTo);
        }
    }

    public static boolean checkNullDataResumeTo(ResumeTo v) {
        String[] line = {v.getTitle(), v.getName(), v.getAddress(), v.getSkills(), v.getUrl(), v.getWorkBefore()};
        for (String text : line) {
            if (isEmpty(text)) {
                log.error(check_error_data, v);
                return false;
            }
        }
        return true;
    }

    public static void checkExistResumeForUser(List<Resume> resumes) {
        if (resumes.size() != 0 && authUserId() != asAdmin().getId()) {
            throw new IllegalArgumentException(resume_exist_already);
        }
    }

    public static void checkNotOwnUpdate(int userId) {
        if (userId != authUserId()) {
            throw new IllegalArgumentException(not_own_data);
        }
    }

    public static void checkNotOwnDelete(int userId) {
        if (userId != authUserId() || userId != asAdmin().getId()) {
            throw new IllegalArgumentException(not_own_data);
        }
    }

    public static String getMessageField(String uri, String value, MessageSourceAccessor messageSourceAccessor) {
        return uri.contains("/rest") ? value : messageSourceAccessor.getMessage(value);
    }
}
