package org.attendantsoffice.eventmanager.user.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.attendantsoffice.eventmanager.authentication.AuthenticationTokenApplicationService;
import org.attendantsoffice.eventmanager.mvc.error.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Handle login failures explicitly. We want to catch the case where they have a recognised user email address but no
 * password set up. In that case we send them an email with the token link to set it.
 * The authentication falls outside the standard exception handling, and the AuthenticationEntryPoint implementation
 * doesn't seem to catch exceptions, so create the error response here using the same response object as the main
 * exception handling.
 */
@Component
public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    private final AuthenticationTokenApplicationService authenticationTokenApplicationService;
    private final ObjectMapper objectMapper;

    public CustomAuthenticationFailureHandler(
            AuthenticationTokenApplicationService authenticationTokenApplicationService,
            ObjectMapper objectMapper) {
        this.authenticationTokenApplicationService = authenticationTokenApplicationService;
        this.objectMapper = objectMapper;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException exception) throws IOException, ServletException {
        ErrorResponse errorResponse;
        if (exception.getCause() != null
                && exception.getCause() instanceof PasswordNotSetAuthenticationException) {
            PasswordNotSetAuthenticationException e = (PasswordNotSetAuthenticationException) exception.getCause();
            sendAuthenticationTokenMail(e);

            errorResponse = new ErrorResponse(HttpStatus.UNAUTHORIZED, "Password is not set", e);
        } else {
            errorResponse = new ErrorResponse(HttpStatus.UNAUTHORIZED, "Authentication failed", exception);
        }

        response.setContentType("application/json");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        objectMapper.writeValue(response.getOutputStream(), errorResponse);
    }

    private void sendAuthenticationTokenMail(PasswordNotSetAuthenticationException exception) {
        authenticationTokenApplicationService.sendAuthenticationTokenMail(exception.getUserId(), exception.getEmail());
    }

}
