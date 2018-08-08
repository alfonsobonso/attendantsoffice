package org.attendantsoffice.eventmanager.authentication;

/**
 * Basic information about the possibly authenticated user.
 */
public class AuthenticationInformation {
    private boolean authenticated;
    private String name;

    public static AuthenticationInformation notAuthenticated() {
        AuthenticationInformation information = new AuthenticationInformation();
        return information;
    }

    public static AuthenticationInformation authenticated(String name) {
        AuthenticationInformation information = new AuthenticationInformation();
        information.authenticated = true;
        information.name = name;
        return information;
    }

    public boolean isAuthenticated() {
        return authenticated;
    }

    public String getName() {
        return name;
    }

}
