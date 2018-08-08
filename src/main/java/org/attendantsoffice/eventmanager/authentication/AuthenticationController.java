package org.attendantsoffice.eventmanager.authentication;

import java.util.Optional;

import org.attendantsoffice.eventmanager.user.security.EventManagerUser;
import org.attendantsoffice.eventmanager.user.security.PasswordNotSetAuthenticationException;
import org.attendantsoffice.eventmanager.user.security.SecurityContext;
import org.attendantsoffice.eventmanager.user.security.UserAuthenticationService;
import org.attendantsoffice.eventmanager.user.security.UserNameNotFoundException;
import org.attendantsoffice.eventmanager.user.security.WrongPasswordException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Create some specific end points that deal with the authentication flow.
 * These are marked as public urls, accessible without authentication
 */
@RestController
public class AuthenticationController {
    private final AuthenticationTokenApplicationService authenticationTokenApplicationService;
    private final UserAuthenticationService userAuthenticationService;

    public AuthenticationController(AuthenticationTokenApplicationService authenticationTokenApplicationService,
            UserAuthenticationService userAuthenticationService) {
        this.authenticationTokenApplicationService = authenticationTokenApplicationService;
        this.userAuthenticationService = userAuthenticationService;
    }

    /**
     * Fetch the authenticated user information. This is a request of the status, so we don't return errors if not
     * authenticated.
     */
    @GetMapping(value = "/authentication")
    public AuthenticationInformation fetchAuthenticationInformation() {
        Optional<EventManagerUser> userDetails = SecurityContext.extractAuthenticatedUser();
        AuthenticationInformation information = userDetails.map(ud -> AuthenticationInformation.authenticated(ud
                .getUsername())).orElse(AuthenticationInformation.notAuthenticated());
        return information;
    }

    /**
     * Attempt to log in.
     * @param email email address
     * @param password password
     * @return authentication token, to be passed in subsequent requests as the Authorization: Bearer header
     */
    @PostMapping("/authentication/login")
    public LoginOutput login(@RequestBody LoginInput loginInput) throws UserNameNotFoundException,
            PasswordNotSetAuthenticationException, WrongPasswordException {
        String token = userAuthenticationService.login(loginInput.getEmail(), loginInput.getPassword());

        return new LoginOutput(token);
    }

    // /**
    // * @return page returned when the user tries to log in but doesn't currently have a password set.
    // */
    // @GetMapping(value = "/authentication/not-authenticated")
    // public String notAuthenticated() {
    // return "authentication/not-authenticated";
    // }
    //
    // /**
    // * @return page returned when the user has clicked on the token link in the email, allowing them to set their
    // * credentials
    // */
    // @GetMapping(value = "/authentication/{token}")
    // public String accessAuthenticationCredentialsForm(@PathVariable String token) {
    //
    // Optional<Integer> userId = authenticationTokenApplicationService.fetchAuthenticationTokenUserId(token);
    //
    // // if the token was not found at all, someone is putting random values in there
    // // we could 404, but we'll be helpful and redirect them to the home page. If they aren't authenticated they will
    // // return to the login page
    //
    // return "authentication/set-credentials";
    // }
}
