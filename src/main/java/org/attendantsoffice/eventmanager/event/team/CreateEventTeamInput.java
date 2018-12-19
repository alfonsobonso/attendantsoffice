package org.attendantsoffice.eventmanager.event.team;

import java.util.Optional;

import javax.validation.constraints.Size;

import org.attendantsoffice.eventmanager.DefaultStyle;
import org.immutables.value.Value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * Team information.
 */
@DefaultStyle
@Value.Immutable
@JsonDeserialize(as = ImmutableCreateEventTeamInput.class)
public interface CreateEventTeamInput {
    @Size(min = 2)
    String getName();

    /**
     * @return the team may be part of the hierarchy, or a top level team.
     */
    Optional<Integer> getParentEventTeamId();

}
