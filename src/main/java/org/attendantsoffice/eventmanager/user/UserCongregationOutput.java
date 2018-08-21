package org.attendantsoffice.eventmanager.user;

import org.attendantsoffice.eventmanager.DefaultStyle;
import org.immutables.value.Value;

/**
 * Basic information about the cong. the user is a member of.
 */
@DefaultStyle
@Value.Immutable
public interface UserCongregationOutput {

    Integer getCongregationId();

    String getName();

}
