package org.attendantsoffice.eventmanager.authentication;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

/**
 * Login credentials request.
 */
public class LoginInput {
    @NotEmpty
    @Email
    private String email;

    @NotEmpty
    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


}

