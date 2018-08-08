package org.attendantsoffice.eventmanager.user.security;

/**
 * The user name (email) was found and has a password set, but the entered password was wrong.
 */
public class WrongPasswordException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private final Integer userId;
    private final String email;

    public WrongPasswordException(Integer userId, String email) {
        super("User#" + userId + " incorrect password entered");
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
