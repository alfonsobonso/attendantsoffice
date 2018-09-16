package org.attendantsoffice.eventmanager.authentication;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import org.attendantsoffice.eventmanager.DefaultStyle;
import org.immutables.value.Value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * Login credentials request.
 */
@DefaultStyle
@Value.Immutable
@JsonDeserialize(as = ImmutableLoginInput.class)
public interface LoginInput {
    @NotEmpty
    @Email
    String getEmail();

    @NotEmpty
    String getPassword();

}

