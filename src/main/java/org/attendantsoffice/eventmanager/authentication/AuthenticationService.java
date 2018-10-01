package org.attendantsoffice.eventmanager.authentication;

import java.util.Optional;

import javax.transaction.Transactional;

import org.apache.commons.lang3.tuple.Pair;
import org.attendantsoffice.eventmanager.user.UserApplicationService;
import org.attendantsoffice.eventmanager.user.UserOutput;
import org.attendantsoffice.eventmanager.user.security.EventManagerUser;
import org.attendantsoffice.eventmanager.user.security.PasswordNotSetAuthenticationException;
import org.attendantsoffice.eventmanager.user.security.UserAuthenticationService;
import org.attendantsoffice.eventmanager.user.security.UserNotFoundException;
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

    /**
     * Request to login using the specified credentials
     */
    public Pair<String, EventManagerUser> login(String email, String password) throws UserNotFoundException,
            PasswordNotSetAuthenticationException,
            WrongPasswordException {
        try {
            return userAuthenticationService.login(email.trim(), password);
        } catch (PasswordNotSetAuthenticationException e) {
            // if they have attempted to authenticate, but they don't actually have a password set,
            // then send them a new access token. The UI should inform them.
            authenticationTokenApplicationService.sendAuthenticationTokenMail(e.getUserId(), e.getEmail());
            throw e;
        }
    }

    /**
     * Request to send an email containing an access token (url) in an email. This is used when the user has no password
     * set, or has forgotten their password.
     */
    public void sendAuthenticationTokenMail(String email) throws UserNotFoundException {
        Optional<UserOutput> user = userApplicationService.findByEmail(email.trim());
        if (!user.isPresent()) {
            throw new UserNotFoundException(email);
        }

        authenticationTokenApplicationService.sendAuthenticationTokenMail(user.get().getUserId(), email);
    }

    /**
     * Look up a user by an access token. This is used as part of the flow of setting/resetting a password, having been
     * email the access code.
     * @see {@code #sendAuthenticationTokenMail(String)}
     */
    public Optional<Integer> fetchAuthenticationTokenUserId(String token) throws AuthenticationTokenExpiredException,
            AuthenticationTokenUsedException {
        return authenticationTokenApplicationService.fetchAuthenticationTokenUserId(token);
    }

    /**
     * Set the user password, using the access token as the authentication.
     * @see {@code #sendAuthenticationTokenMail(String)}
     * @see {@code #fetchAuthenticationTokenUserId(String)}
     */
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
