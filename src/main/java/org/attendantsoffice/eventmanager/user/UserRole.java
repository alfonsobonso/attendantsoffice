package org.attendantsoffice.eventmanager.user;

/**
 * Information about the security access of the user.
 */
public enum UserRole {
    /**
     * Basic user - will have access to their assignment information, and possibly their team if they are a captain.
     */
    USER,

    /**
     * Full access to users, events, congregations, etc
     */
    ADMIN
}
