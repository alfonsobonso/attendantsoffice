package org.attendantsoffice.eventmanager.authentication;

import org.attendantsoffice.eventmanager.DefaultStyle;
import org.immutables.value.Value;
import org.immutables.value.Value.Parameter;

/**
 * Response indicating the status a given access token
 */
@DefaultStyle
@Value.Immutable
public interface AccessTokenStatusOutput {
    @Parameter
    Status getStatus();

    public enum Status {
        VALID,
        /**
         * We have no record of the access token
         */
        UNRECOGNISED,
        /**
         * The token was issued too long ago
         */
        EXPIRED,
        /**
         * the token has already been used ot set a password
         */
        ALREADY_USED;
    }
}
