package org.attendantsoffice.eventmanager.user;

import java.util.Optional;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

import org.attendantsoffice.eventmanager.DefaultStyle;
import org.immutables.value.Value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * User information, formatted for the API. This does not include some values which we default for a new user.
 * @see {@code UpdateUserInput}
 */
@DefaultStyle
@Value.Immutable
@JsonDeserialize(as = ImmutableCreateUserInput.class)
public interface CreateUserInput {
    @Size(min = 2)
    String getFirstName();

    @Size(min = 2)
    String getLastName();

    Optional<String> getHomePhone();

    Optional<String> getMobilePhone();

    @Email
    String getEmail();

    Integer getCongregationId();

    UserPosition getPosition();
}
