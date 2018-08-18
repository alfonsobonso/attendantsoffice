package org.attendantsoffice.eventmanager.user.security;

import java.util.Optional;

import org.apache.commons.lang3.tuple.Pair;

/**
 * Look up the authenticated user based on the token
 */
public interface UserAuthenticationService {
    /**
     * Logs in with the given {@code email} and {@code password}.
     * @param email
     * @param password
     * @return the user authentication token when login succeeds, along with the user information
     * @throws UserNotFoundException when the user name does not match any user
     * @throws PasswordNotSetAuthenticationException when the user is matched but they have not set a password
     * @throws WrongPasswordException when the user is matched but the password was wrong
     */
    Pair<String, EventManagerUser> login(String email, String password) throws UserNotFoundException,
            PasswordNotSetAuthenticationException,
            WrongPasswordException;

    /**
     * Finds a user by the authentication token.
     */
    Optional<EventManagerUser> findByToken(String token);

    /**
     * Logs out the given user, remove any current authentication tokens
     */
    void logout(Integer userId);
}
