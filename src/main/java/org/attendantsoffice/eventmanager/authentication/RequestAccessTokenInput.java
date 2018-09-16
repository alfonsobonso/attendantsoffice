package org.attendantsoffice.eventmanager.authentication;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import org.attendantsoffice.eventmanager.DefaultStyle;
import org.immutables.value.Value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * Request made when asking for a new access token.
 */
@DefaultStyle
@Value.Immutable
@JsonDeserialize(as = ImmutableRequestAccessTokenInput.class)
public interface RequestAccessTokenInput {
    @NotEmpty
    @Email
    public String getEmail();
}
