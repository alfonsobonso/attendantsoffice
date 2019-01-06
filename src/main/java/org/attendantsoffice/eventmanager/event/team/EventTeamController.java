package org.attendantsoffice.eventmanager.event.team;

import java.util.List;

import org.attendantsoffice.eventmanager.mvc.error.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EventTeamController {
    private static final Logger LOG = LoggerFactory.getLogger(EventTeamController.class);
    private final EventTeamApplicationService eventTeamApplicationService;

    public EventTeamController(EventTeamApplicationService eventTeamApplicationService) {
        this.eventTeamApplicationService = eventTeamApplicationService;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(path = "/event-teams/{eventTeamId}")
    public EventTeamOutput findEventTeam(@PathVariable int eventTeamId) {
        EventTeamSearchCriteria searchCriteria = new EventTeamSearchCriteria();
        searchCriteria.setEventTeamId(eventTeamId);

        List<EventTeamOutput> eventTeams = eventTeamApplicationService.findEventTeams(searchCriteria);
        if (eventTeams.isEmpty()) {
            throw new IllegalArgumentException("EventTeam#" + eventTeamId + " is not found");
        }
        return eventTeams.get(0);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(path = "/events/{eventId}/teams")
    public EventTeamOutput createEventTeam(@PathVariable int eventId, @RequestBody CreateEventTeamInput input) {
        return eventTeamApplicationService.createEventTeam(eventId, input);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(path = "/events/{eventId}/teams")
    public List<EventTeamOutput> findEventTeams(@PathVariable int eventId) {
        EventTeamSearchCriteria searchCriteria = new EventTeamSearchCriteria();
        searchCriteria.setEventId(eventId);

        return eventTeamApplicationService.findEventTeams(searchCriteria);
    }

    @ExceptionHandler(DuplicateEventTeamNameException.class)
    protected ResponseEntity<Object> handleDuplicateEventTeamNameException(Exception ex) {
        LOG.error(ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage(), ex);
        return new ResponseEntity<>(errorResponse, errorResponse.getStatus());
    }
}
