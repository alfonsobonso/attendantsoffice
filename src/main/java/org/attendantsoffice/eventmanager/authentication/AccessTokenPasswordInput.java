package org.attendantsoffice.eventmanager.authentication;

import org.attendantsoffice.eventmanager.DefaultStyle;
import org.immutables.value.Value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * Password submitted when the user uses the access token to authenticate
 */
@DefaultStyle
@Value.Immutable
@JsonDeserialize(as = ImmutableAccessTokenPasswordInput.class)
public interface AccessTokenPasswordInput {
    String getPassword();

}
