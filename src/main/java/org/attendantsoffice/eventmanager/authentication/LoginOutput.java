package org.attendantsoffice.eventmanager.authentication;

/**
 * Encapsulate the login authentication token in the login endpoint response.
 */
public class LoginOutput {
    private final String token;

    public LoginOutput(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

}
