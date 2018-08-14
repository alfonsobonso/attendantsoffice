package org.attendantsoffice.eventmanager.authentication;

/**
 * Response indicating the status a given access token
 */
public class AccessTokenStatusOutput {
    private final Status status;

    public AccessTokenStatusOutput(Status status) {
        this.status = status;
    }

    public Status getStatus() {
        return status;
    }

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
