package org.attendantsoffice.eventmanager.authentication;

import java.util.Optional;

import org.attendantsoffice.eventmanager.user.security.PasswordNotSetAuthenticationException;
import org.attendantsoffice.eventmanager.user.security.UserNotFoundException;
import org.attendantsoffice.eventmanager.user.security.WrongPasswordException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Create some specific end points that deal with the authentication flow.
 * These are marked as public urls, accessible without authentication.
 */
@RestController
public class AuthenticationController {
    private static final Logger LOG = LoggerFactory.getLogger(AuthenticationController.class);
    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    /**
     * Attempt to log in.
     * @param email email address
     * @param password password
     * @return authentication token, to be passed in subsequent requests as the Authorization: Bearer header
     */
    @PostMapping("/authentication/login")
    public LoginOutput login(@RequestBody LoginInput loginInput) throws UserNotFoundException,
            PasswordNotSetAuthenticationException, WrongPasswordException {
        String token = authenticationService.login(loginInput.getEmail(), loginInput.getPassword());

        return new LoginOutput(token);
    }

    /**
     * Request an access token be sent to the submitted email.
     */
    @PostMapping("/authentication/access-token")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void requestAccessToken(@RequestBody RequestAccessTokenInput input) throws UserNotFoundException {
        authenticationService.sendAuthenticationTokenMail(input.getEmail().trim());
    }

    /**
     * Determine whether the given access token is valid. This would be uses as a check before using this to
     * set a password on a previously unauthenticated user, or one who has forgotten their password.
     */
    @GetMapping(value = "/authentication/access-token/{token}/status")
    public AccessTokenStatusOutput fetchAccessTokenStatus(@PathVariable String token) {
        AccessTokenStatusOutput output;
        try {
            Optional<Integer> userId = authenticationService.fetchAuthenticationTokenUserId(token);
            if (!userId.isPresent()) {
                LOG.warn("Authentication access token [{}] has unrecognised", token);
                output = new AccessTokenStatusOutput(AccessTokenStatusOutput.Status.UNRECOGNISED);
            } else {
                output = new AccessTokenStatusOutput(AccessTokenStatusOutput.Status.VALID);
            }
        } catch(AuthenticationTokenExpiredException e) {
            LOG.warn("User#{} authentication access token [{}] has expired", e.getUserId(), token);
            output = new AccessTokenStatusOutput(AccessTokenStatusOutput.Status.EXPIRED);
        } catch (AuthenticationTokenUsedException e) {
            LOG.warn("User#{} authentication access token [{}] has already been used", e.getUserId(), token);
            output = new AccessTokenStatusOutput(AccessTokenStatusOutput.Status.ALREADY_USED);
        }
        return output;
    }

    /**
     * Submit a new password using access token credentials
     * As a result of this the token is marked as used the user password is updated, and the user is marked as not
     * authenticated. We expect the client to redirect the user to the login screen.
     */
    @PostMapping(value = "/authentication/access-token/{token}/update-password")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void submitAccessTokenPassword(@PathVariable String token, @RequestBody AccessTokenPasswordInput input) {
        authenticationService.submitAccessTokenPassword(token, input.getPassword());
    }

}
