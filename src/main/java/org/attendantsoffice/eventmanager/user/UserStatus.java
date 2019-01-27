package org.attendantsoffice.eventmanager.user;

/**
 * The user status and assignment status work together to indicate if a volunteer will be available to take up and assignment.
 * Generally, a user being marked as unavailable here means the assignment status is also unavailable. There may be an edge case
 * where, due to circumstance, a volunteer who has multiple assignments, may be available, but have one or more of their assignments 
 * marked as unavailable.
 * @see {@code org.attendantsoffice.eventmanager.event.assigment.AssignmentStatus}
 */
public enum UserStatus {
    /**
     * May be asked. The congregation approval is marked against the assignment, so active does not mean they can be
     * used for sure.
     */
    AVAILABLE,

    /**
     * Not to be used, for an unspecified reason.
     */
    UNAVAILABLE
}
