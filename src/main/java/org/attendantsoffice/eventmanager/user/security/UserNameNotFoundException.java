package org.attendantsoffice.eventmanager.user.security;

/**
 * The user name (email) was not found.
 */
public class UserNameNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private final String email;

    public UserNameNotFoundException(String email) {
        super();
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

}
