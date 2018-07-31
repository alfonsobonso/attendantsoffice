package org.attendantsoffice.eventmanager.authentication;

/**
 * Thrown when an accessed authentication token is too old.
 * We can still identify them from the token, but we don't allow them to set their credentials with it.
 */
public class AuthenticationTokenExpiredException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private final String token;
    private final Integer userId;

    public AuthenticationTokenExpiredException(String token, Integer userId) {
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
