package org.attendantsoffice.eventmanager.event.team;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.attendantsoffice.eventmanager.event.EventEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Fetch/update the underlying event team information.
 * We expect few entries here, so we cache this as a complete list
 */
@Service
@Transactional
public class EventTeamApplicationService {
    private final EventTeamRepository eventTeamRepository;
    private final EventTeamMapper eventTeamMapper;
    private final EventTeamParentValidator eventTeamParentValidator;

    public EventTeamApplicationService(EventTeamRepository eventTeamRepository,
            EventTeamMapper eventTeamMapper,
            EventTeamParentValidator eventTeamParentValidator) {
        this.eventTeamRepository = eventTeamRepository;
        this.eventTeamMapper = eventTeamMapper;
        this.eventTeamParentValidator = eventTeamParentValidator;
    }

    @Transactional(readOnly = true)
    public List<EventTeamOutput> findEventTeams(EventTeamSearchCriteria searchCriteria) {
        List<EventTeamEntity> entityList = eventTeamRepository.findAllEventTeams();
        List<EventTeamOutput> outputList = entityList.stream()
                .filter(e -> searchCriteria.getEventTeamId() == null
                        || searchCriteria.getEventTeamId().equals(e.getEventTeamId()))
                .filter(e -> searchCriteria.getEventId() == null
                        || searchCriteria.getEventId().equals(e.getEvent().getEventId()))
                .map(e -> eventTeamMapper.map(e, entityList))
                .collect(Collectors.toList());
        return outputList;
    }

    public EventTeamOutput createEventTeam(int eventId, CreateEventTeamInput input) {
        List<EventTeamEntity> allEventTeams = eventTeamRepository.findAllEventTeams();

        // validate we don't have a duplicate name
        Optional<EventTeamEntity> matchingNamedTeam = allEventTeams.stream()
                .filter(team -> team.getEvent().getEventId().equals(eventId))
                .filter(team -> team.getName().equalsIgnoreCase(input.getName()))
                .findAny();
        if (matchingNamedTeam.isPresent()) {
            throw new DuplicateEventTeamNameException(matchingNamedTeam.get().getEventTeamId(),
                    matchingNamedTeam.get().getName());
        }

        EventTeamEntity entity = new EventTeamEntity();

        EventEntity eventEntity = new EventEntity();
        eventEntity.setEventId(eventId);
        entity.setEvent(eventEntity);
        entity.setName(input.getName());
        entity.setNameWithCaptain(input.getName()); // captain not yet set
        entity.setParentEventTeamId(input.getParentEventTeamId().orElse(null));

        eventTeamParentValidator.assertEventTeamParentValid(entity, allEventTeams);

        eventTeamRepository.saveEventTeam(entity);

        EventTeamOutput output = eventTeamMapper.map(entity, allEventTeams);
        return output;
    }

    @Transactional(readOnly = true)
    public String findName(Integer eventTeamId) {
        List<EventTeamEntity> eventTeams = eventTeamRepository.findAllEventTeams();

        EventTeamEntity entity = eventTeams.stream()
                .filter(c -> c.getEventTeamId().equals(eventTeamId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown EventTeam#" + eventTeamId));
        return entity.getName();
    }

}
