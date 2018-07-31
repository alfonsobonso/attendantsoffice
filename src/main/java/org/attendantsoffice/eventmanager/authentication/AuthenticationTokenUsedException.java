package org.attendantsoffice.eventmanager.authentication;

/**
 * Thrown when an accessed authentication token has already been used to set their credentials.
 * We can still identify them from the token, but we don't allow them to set their credentials with it again.
 */
public class AuthenticationTokenUsedException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private final String token;
    private final Integer userId;

    public AuthenticationTokenUsedException(String token, Integer userId) {
        this.token = token;
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public Integer getUserId() {
        return userId;
    }

}
