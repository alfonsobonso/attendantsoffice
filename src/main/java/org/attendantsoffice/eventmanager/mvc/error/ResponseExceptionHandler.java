package org.attendantsoffice.eventmanager.mvc.error;

import org.attendantsoffice.eventmanager.user.security.PasswordNotSetAuthenticationException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
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

    @ExceptionHandler(AuthenticationException.class)
    protected ResponseEntity<Object> handleAuthenticationException(AuthenticationException ex) {
        return buildResponseEntity(new ErrorResponse(HttpStatus.UNAUTHORIZED, "Password not set", ex));
    }

    @ExceptionHandler(BadCredentialsException.class)
    protected ResponseEntity<Object> handleBadCredentialsException(BadCredentialsException ex) {
        return buildResponseEntity(new ErrorResponse(HttpStatus.BAD_REQUEST, "Bad credentials", ex));
    }

    // @ExceptionHandler(NoHandlerFoundException.class)
    // protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex) {
    // return buildResponseEntity(new ErrorResponse(HttpStatus.NOT_FOUND, "No handler found", ex));
    // }
    //
    // @ExceptionHandler(Exception.class)
    // protected ResponseEntity<Object> handleException(Exception ex) {
    // return buildResponseEntity(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex));
    // }

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
