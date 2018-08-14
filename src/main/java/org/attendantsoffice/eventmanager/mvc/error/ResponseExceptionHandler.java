package org.attendantsoffice.eventmanager.mvc.error;

import org.attendantsoffice.eventmanager.user.security.PasswordNotSetAuthenticationException;
import org.attendantsoffice.eventmanager.user.security.UserNotFoundException;
import org.attendantsoffice.eventmanager.user.security.WrongPasswordException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Order(Ordered.LOWEST_PRECEDENCE)
@RestControllerAdvice
public class ResponseExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(PasswordNotSetAuthenticationException.class)
    protected ResponseEntity<Object> handlePasswordNotSetAuthenticationException(
            PasswordNotSetAuthenticationException ex) {
        return buildResponseEntity(new ErrorResponse(HttpStatus.UNPROCESSABLE_ENTITY, "Password not set", ex));
    }

    @ExceptionHandler(UserNotFoundException.class)
    protected ResponseEntity<Object> handleUserNotFoundException(UserNotFoundException ex) {
        return buildResponseEntity(new ErrorResponse(HttpStatus.UNAUTHORIZED, "User not found", ex));
    }

    @ExceptionHandler(WrongPasswordException.class)
    protected ResponseEntity<Object> handleWrongPasswordException(WrongPasswordException ex) {
        return buildResponseEntity(new ErrorResponse(HttpStatus.UNAUTHORIZED, "Bad credentials", ex));
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers,
            HttpStatus status, WebRequest request) {
        return buildResponseEntity(new ErrorResponse(HttpStatus.NOT_FOUND, "No handler found", ex));
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return buildResponseEntity(new ErrorResponse(HttpStatus.BAD_REQUEST, "Bad request", ex));
    }

    private ResponseEntity<Object> buildResponseEntity(ErrorResponse errorResponse) {
        return new ResponseEntity<>(errorResponse, errorResponse.getStatus());
    }

}
