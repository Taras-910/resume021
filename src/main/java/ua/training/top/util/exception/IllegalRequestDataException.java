package ua.training.top.util.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "wrong request")  // 409
public class IllegalRequestDataException extends RuntimeException {
    public IllegalRequestDataException(String msg) {
        super(msg);
    }
}

