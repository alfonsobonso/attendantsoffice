package org.attendantsoffice.eventmanager.event.team;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EventTeamController {

    private final EventTeamApplicationService eventTeamApplicationService;

    public EventTeamController(EventTeamApplicationService eventTeamApplicationService) {
        this.eventTeamApplicationService = eventTeamApplicationService;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(path = "/events/{eventId}/teams")
    public List<EventTeamOutput> findEventTeams(@PathVariable int eventId) {
        EventTeamSearchCriteria searchCriteria = new EventTeamSearchCriteria();
        searchCriteria.setEventId(eventId);

        return eventTeamApplicationService.findEventTeams(searchCriteria);
    }
}
