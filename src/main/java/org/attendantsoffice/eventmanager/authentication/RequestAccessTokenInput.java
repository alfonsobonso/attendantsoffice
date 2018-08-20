package org.attendantsoffice.eventmanager.authentication;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

/**
 * Request made when asking for a new access token.
 */
public class RequestAccessTokenInput {
    @NotEmpty
    @Email
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
