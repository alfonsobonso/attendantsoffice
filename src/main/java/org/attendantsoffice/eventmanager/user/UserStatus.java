package org.attendantsoffice.eventmanager.user;

public enum UserStatus {
    /**
     * May be asked. The congregation approval is marked against the assignment, so active does not mean they can be
     * used for sure.
     */
    ACTIVE,

    /**
     * Not to be used.
     */
    DISABLED
}
