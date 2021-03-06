package org.attendantsoffice.eventmanager.authentication;

import org.attendantsoffice.eventmanager.DefaultStyle;
import org.immutables.value.Value;

/**
 * Encapsulate the login authentication token in the login endpoint response.
 */
@DefaultStyle
@Value.Immutable
public interface LoginOutput {
    /**
     * @return authentication token. This should be passed as the auth bearer in subsequent requests.
     */
    String getToken();

    /**
     * @return unique user identifier
     */
    int getUserId();

    /**
     * @return email address of the user
     */
    String getEmail();

    /**
     * @return user first name
     */
    String getFirstName();

    /**
     * @return user last name
     */
    String getLastName();

    /**
     * Granted system role - currently ROLE_USER or ROLE_ADMIN.
     * @see {@code org.attendantsoffice.eventmanager.user.UserRole}
     */
    String getRole();

}
