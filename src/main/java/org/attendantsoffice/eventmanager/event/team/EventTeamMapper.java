package org.attendantsoffice.eventmanager.event.team;

import java.util.List;
import java.util.Optional;

import org.attendantsoffice.eventmanager.common.list.EntityListOutput;
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

    /**
     * Map the team to the output.
     * @param entity event team to map
     * @param eventTeams all event teams, used to populate the parent team information.
     * @return output
     */
    public EventTeamOutput map(EventTeamEntity entity, List<EventTeamEntity> eventTeams) {
        // the event is lazy-loaded. We have cached the lookup of all events already, so save a join here.
        ImmutableEntityListOutput eventOutput = ImmutableEntityListOutput.of(
                entity.getEvent().getEventId(),
                eventApplicationService.findName(entity.getEvent().getEventId()));

        Optional<EntityListOutput> parentEventTeam = Optional.ofNullable(entity.getParentEventTeamId()).map(
                parentId -> findEventTeamParent(parentId, eventTeams));

        EventTeamOutput output = ImmutableEventTeamOutput.builder()
                .eventTeamId(entity.getEventTeamId())
                .event(eventOutput)
                .name(entity.getName())
                .nameWithCaptain(entity.getNameWithCaptain())
                .parentEventTeam(parentEventTeam)
                .build();
        return output;
    }


    /**
     * Look up the parent team. We expect the referential integrity to mean this sohuld be set
     */
    private EntityListOutput findEventTeamParent(Integer parentId, List<EventTeamEntity> eventTeams) {
        EventTeamEntity parent = eventTeams.stream()
                .filter(et -> et.getEventTeamId().equals(parentId))
                .findAny()
                .orElseThrow(() -> new IllegalStateException("EventTeam parent#" + parentId + " was not found"));
        return ImmutableEntityListOutput.of(parentId, parent.getNameWithCaptain());
    }

}
