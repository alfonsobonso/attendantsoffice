package org.attendantsoffice.eventmanager.authentication;

import java.util.Optional;

import org.attendantsoffice.eventmanager.user.security.CustomUserDetails;
import org.attendantsoffice.eventmanager.user.security.SecurityContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Create some specific end points that deal with the authentication flow
 */
@RestController
public class AuthenticationController {
    private final AuthenticationTokenApplicationService authenticationTokenApplicationService;

    public AuthenticationController(AuthenticationTokenApplicationService authenticationTokenApplicationService) {
        this.authenticationTokenApplicationService = authenticationTokenApplicationService;
    }

    /**
     * Fetch the authenticated user information. This is a request of the status, so we don't return errors if not
     * authenticated.
     */
    @GetMapping(value = "/authentication")
    public AuthenticationInformation fetchAuthenticationInformation() {
        Optional<CustomUserDetails> userDetails = SecurityContext.extractAuthenticatedUser();
        AuthenticationInformation information = userDetails.map(ud -> AuthenticationInformation.authenticated(ud
                .getUsername())).orElse(AuthenticationInformation.notAuthenticated());
        return information;
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
