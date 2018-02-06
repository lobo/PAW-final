package ar.edu.itba.paw.model.Exception;

import org.jetbrains.annotations.NonNls;

/**
 * Created by juanfra on 18/06/17.
 */
public class ValidatorException extends RuntimeException {
    public ValidatorException() {
    }

    public ValidatorException(@NonNls String message) {
        super(message);
    }

    public ValidatorException(String message, Throwable cause) {
        super(message, cause);
    }

    public ValidatorException(Throwable cause) {
        super(cause);
    }

    public ValidatorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
