package org.attendantsoffice.eventmanager.event.team;

import java.util.Optional;

import org.attendantsoffice.eventmanager.DefaultStyle;
import org.immutables.value.Value;
import org.immutables.value.Value.Check;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.Preconditions;

/**
 * Team information.
 */
@DefaultStyle
@Value.Immutable
@JsonDeserialize(as = ImmutableUpdateEventTeamInput.class)
public interface UpdateEventTeamInput {
    String getName();

    /**
     * @return the team may be part of the hierarchy, or a top level team.
     */
    Optional<Integer> getParentEventTeamId();

    @Check
    default void check() {
        Preconditions.checkArgument(getName().trim().length() >= 2, "Name must be 2 characters or longer");
    }
}
