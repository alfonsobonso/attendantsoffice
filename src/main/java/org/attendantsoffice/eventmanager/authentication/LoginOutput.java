package org.attendantsoffice.eventmanager.authentication;

/**
 * Encapsulate the login authentication token in the login endpoint response.
 */
public class LoginOutput {
    private final int userId;
    private final String firstName;
    private final String lastName;
    private final String token;

    public LoginOutput(int userId, String firstName, String lastName, String token) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.token = token;
    }

    /**
     * @return authentication token. This should be passed as the auth bearer in subsequent requests.
     */
    public String getToken() {
        return token;
    }

    /**
     * @return unique user identifier
     */
    public int getUserId() {
        return userId;
    }

    /**
     * @return user first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * @return user last name
     */
    public String getLastName() {
        return lastName;
    }

}
