package org.attendantsoffice.eventmanager.authentication;

import java.util.Optional;

import javax.transaction.Transactional;

import org.attendantsoffice.eventmanager.user.UserApplicationService;
import org.attendantsoffice.eventmanager.user.security.PasswordNotSetAuthenticationException;
import org.attendantsoffice.eventmanager.user.security.UserAuthenticationService;
import org.attendantsoffice.eventmanager.user.security.UserNameNotFoundException;
import org.attendantsoffice.eventmanager.user.security.WrongPasswordException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Define the actions around authenticating users.
 * This provides transaction controls around the token/user password changes.
 */
@Service
@Transactional
public class AuthenticationService {
    private final AuthenticationTokenApplicationService authenticationTokenApplicationService;
    private final UserAuthenticationService userAuthenticationService;
    private final UserApplicationService userApplicationService;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationService(AuthenticationTokenApplicationService authenticationTokenApplicationService,
            UserAuthenticationService userAuthenticationService,
            UserApplicationService userApplicationService,
            PasswordEncoder passwordEncoder) {
        this.authenticationTokenApplicationService = authenticationTokenApplicationService;
        this.userAuthenticationService = userAuthenticationService;
        this.userApplicationService = userApplicationService;
        this.passwordEncoder = passwordEncoder;
    }

    public String login(String email, String password) throws UserNameNotFoundException,
            PasswordNotSetAuthenticationException,
            WrongPasswordException {
        return userAuthenticationService.login(email, password);
    }

    public Optional<Integer> fetchAuthenticationTokenUserId(String token) throws AuthenticationTokenExpiredException,
            AuthenticationTokenUsedException {
        return authenticationTokenApplicationService.fetchAuthenticationTokenUserId(token);
    }

    @Transactional
    public void submitAccessTokenPassword(@PathVariable String token, String password) {
        Integer userId = authenticationTokenApplicationService.fetchAuthenticationTokenUserId(token)
                .orElseThrow(() -> new IllegalArgumentException("No user matches token [" + token + "]"));

        authenticationTokenApplicationService.markAuthenticationTokenUsed(token);
        String encodedPassword = passwordEncoder.encode(password);
        userApplicationService.updatePassword(userId, encodedPassword);
        userAuthenticationService.logout(userId);
    }

}
