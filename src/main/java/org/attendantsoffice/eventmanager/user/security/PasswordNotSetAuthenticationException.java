package org.attendantsoffice.eventmanager.user.security;

/**
 * When the user has been created but there is no password defined, we want to inform the user and allow them to send a
 * create password link to their email.
 */
public class PasswordNotSetAuthenticationException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private final Integer userId;
    private final String email;

    public PasswordNotSetAuthenticationException(Integer userId, String email) {
        super("User#" + userId + " has no password defined");
        this.userId = userId;
        this.email = email;
    }

    public Integer getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

}
