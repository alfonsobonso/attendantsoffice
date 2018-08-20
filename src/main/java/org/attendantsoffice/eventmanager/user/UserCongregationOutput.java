package org.attendantsoffice.eventmanager.user;

import org.immutables.value.Value;

/**
 * Basic information about the cong. the user is a member of.
 */
@Value.Immutable
public interface UserCongregationOutput {

    Integer getCongregationId();

    String getName();

}
