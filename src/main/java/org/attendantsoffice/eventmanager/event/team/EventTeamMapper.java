package org.attendantsoffice.eventmanager.event.team;

import org.attendantsoffice.eventmanager.common.list.ImmutableEntityListOutput;
import org.attendantsoffice.eventmanager.event.EventApplicationService;
import org.springframework.stereotype.Component;

/**
 * Map between the underlying EventTeamEntity and the EventTeamOutput instance
 *
 */
@Component
public class EventTeamMapper {
    private final EventApplicationService eventApplicationService;

    public EventTeamMapper(EventApplicationService eventApplicationService) {
        this.eventApplicationService = eventApplicationService;
    }

    public EventTeamOutput map(EventTeamEntity entity) {
        // the event is lazy-loaded. We have cached the lookup of all events already, so save a join here.
        ImmutableEntityListOutput eventOutput = ImmutableEntityListOutput.of(
                entity.getEvent().getEventId(),
                eventApplicationService.findName(entity.getEvent().getEventId()));

        EventTeamOutput output = ImmutableEventTeamOutput.builder()
                .eventTeamId(entity.getEventTeamId())
                .event(eventOutput)
                .name(entity.getName())
                .nameWithCaptain(entity.getNameWithCaptain())
                // .parentEventTeam(parentEventTeam) TBD - work out the recursion
                .build();
        return output;
    }

}
