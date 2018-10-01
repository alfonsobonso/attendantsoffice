package org.attendantsoffice.eventmanager.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Indicate a new user, or an existing one changing their email address, is clashing with another user.
 */
@ResponseStatus(code = HttpStatus.UNPROCESSABLE_ENTITY)
public class DuplicateUserEmailAddressException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public DuplicateUserEmailAddressException(Integer userId, String firstName, String lastName, String emailAddress) {
        super("Duplicate email address. " + emailAddress + " matches existing User#" + userId + ": " + firstName + " "
                + lastName);
    }

}
