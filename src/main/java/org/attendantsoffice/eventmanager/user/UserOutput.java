package org.attendantsoffice.eventmanager.user;

import java.util.Optional;

import org.attendantsoffice.eventmanager.DefaultStyle;
import org.immutables.value.Value;

/**
 * User information, formatted for the API.
 */
@DefaultStyle
@Value.Immutable
public interface UserOutput {
    Integer getUserId();

    String getFirstName();

    String getLastName();

    Optional<String> getHomePhone();

    Optional<String> getMobilePhone();

    String getEmail();

    UserCongregationOutput getCongregation();
}
