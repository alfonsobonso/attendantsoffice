package org.attendantsoffice.eventmanager.authentication;

/**
 * Password submitted when the user uses the access token to authenticate
 */
public class AccessTokenPasswordInput {
    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
