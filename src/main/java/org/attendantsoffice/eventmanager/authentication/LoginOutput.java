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
    public String getToken();

    /**
     * @return unique user identifier
     */
    public int getUserId();

    /**
     * @return user first name
     */
    public String getFirstName();

    /**
     * @return user last name
     */
    public String getLastName();

}
