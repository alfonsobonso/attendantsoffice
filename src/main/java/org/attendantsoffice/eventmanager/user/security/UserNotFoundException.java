package org.attendantsoffice.eventmanager.user.security;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * The user (email) was not found.
 */
@ResponseStatus(code = HttpStatus.FORBIDDEN)
public class UserNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private final String email;

    public UserNotFoundException(String email) {
        super();
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

}
