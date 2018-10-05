package org.attendantsoffice.eventmanager.event.team;

import java.util.Optional;

import org.attendantsoffice.eventmanager.DefaultStyle;
import org.attendantsoffice.eventmanager.common.list.EntityListOutput;
import org.immutables.value.Value;

/**
 * Event team information, formatted for the API.
 */
@DefaultStyle
@Value.Immutable
public interface EventTeamOutput {

    public Integer getEventTeamId();

    public EntityListOutput getEvent();

    /**
     * @return explicitly entered name, e.g "Attendants HQ"
     * @see {@code #getNameWithCaptain()}
     */
    public String getName();

    /**
     * @return derived name. For convenience, we create the derived name from the team name and the assigned captain,
     * since this is how it is often searched for and displayed. The application will need to contain the logic of
     * making sure team name change, captain name changes or captain assignment changes are reflected here.
     */
    public String getNameWithCaptain();

    /**
     * @return optional team hierarchy.
     */
    public Optional<EntityListOutput> getParentEventTeam();

}
