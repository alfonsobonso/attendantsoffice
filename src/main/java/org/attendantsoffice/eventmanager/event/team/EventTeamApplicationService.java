package org.attendantsoffice.eventmanager.event.team;

import java.util.List;
import java.util.stream.Collectors;

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

    public EventTeamApplicationService(EventTeamRepository eventTeamRepository, EventTeamMapper eventTeamMapper) {
        this.eventTeamRepository = eventTeamRepository;
        this.eventTeamMapper = eventTeamMapper;
    }

    @Transactional(readOnly = true)
    public List<EventTeamOutput> findEventTeams(EventTeamSearchCriteria searchCriteria) {
        List<EventTeamEntity> entityList = eventTeamRepository.findAllEventTeams();
        List<EventTeamOutput> outputList = entityList.stream()
                .filter(e -> searchCriteria.getEventId() == null
                        || searchCriteria.getEventId().equals(e.getEvent().getEventId()))
                .map(e -> eventTeamMapper.map(e, entityList))
                .collect(Collectors.toList());
        return outputList;
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
