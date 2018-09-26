package org.attendantsoffice.eventmanager.user;

import java.util.Optional;

import org.attendantsoffice.eventmanager.DefaultStyle;
import org.attendantsoffice.eventmanager.common.list.EntityListOutput;
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

    EntityListOutput getCongregation();

    UserStatus getUserStatus();

    UserPosition getPosition();

    UserRole getRole();

}
