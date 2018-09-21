package org.attendantsoffice.eventmanager.mvc.error;

import org.attendantsoffice.eventmanager.user.security.PasswordNotSetAuthenticationException;
import org.attendantsoffice.eventmanager.user.security.UserNotFoundException;
import org.attendantsoffice.eventmanager.user.security.WrongPasswordException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.TypeMismatchException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;

@Order(Ordered.LOWEST_PRECEDENCE)
@RestControllerAdvice
public class ResponseExceptionHandler {
    private static final Logger LOG = LoggerFactory.getLogger(ResponseExceptionHandler.class);

    // application specific exceptions
    @ExceptionHandler(PasswordNotSetAuthenticationException.class)
    protected ResponseEntity<Object> handlePasswordNotSetAuthenticationException(
            PasswordNotSetAuthenticationException ex) {
        return handleException(HttpStatus.UNPROCESSABLE_ENTITY, "Password not set", ex, false);
    }

    @ExceptionHandler(UserNotFoundException.class)
    protected ResponseEntity<Object> handleUserNotFoundException(UserNotFoundException ex) {
        return handleException(HttpStatus.UNAUTHORIZED, "User not found", ex, false);
    }

    @ExceptionHandler(WrongPasswordException.class)
    protected ResponseEntity<Object> handleWrongPasswordException(WrongPasswordException ex) {
        return handleException(HttpStatus.UNAUTHORIZED, "Bad credentials", ex, false);
    }

    // generic exceptions around the spring framework

    @ExceptionHandler({
        IllegalArgumentException.class,
        HttpMessageNotReadableException.class,
        MissingServletRequestParameterException.class,
        ServletRequestBindingException.class,
        TypeMismatchException.class,
        MethodArgumentNotValidException.class,
        MissingServletRequestPartException.class,
        BindException.class
    })
    protected ResponseEntity<Object> handleBadRequestException(Exception ex) {
        return handleException(HttpStatus.BAD_REQUEST, ex, true);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<Object> handleMethodNotAllowed(Exception ex) {
        return handleException(HttpStatus.METHOD_NOT_ALLOWED, ex, true);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    protected ResponseEntity<Object> handleNotFound(Exception ex) {
        return handleException(HttpStatus.NOT_FOUND, ex, false);
    }

    // this is the catch-all
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handleInternalServiceError(Exception ex) {
        return handleException(HttpStatus.INTERNAL_SERVER_ERROR, ex, true);
    }

    /**
     * Handle the exception, generating the message from the exception class name.
     */
    private ResponseEntity<Object> handleException(HttpStatus status, Exception ex, boolean logStackTrace) {
        return handleException(status, extractExceptionName(ex), ex, logStackTrace);
    }

    /**
     * Handle the exception, with an explicit message set.
     * Log if requested, and return the error response to the client.
     */
    private ResponseEntity<Object> handleException(HttpStatus status, String message, Exception ex,
            boolean logStackTrace) {
        if (logStackTrace) {
            LOG.error("Request failed:", ex);
        }

        ErrorResponse errorResponse = new ErrorResponse(status, message, ex);
        return new ResponseEntity<>(errorResponse, errorResponse.getStatus());
    }

    /**
     * Convert the exception name into something readable, e.g NoHandlerFoundException becomes "No Handler Found"
     */
    private String extractExceptionName(Exception ex) {
        String name = ex.getClass().getSimpleName().replaceAll("Exception", "").replaceAll("([A-Z])", " $1");
        return name;
    }

}
