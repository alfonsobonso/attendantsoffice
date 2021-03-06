package org.attendantsoffice.eventmanager.event;

import java.util.List;

import javax.validation.Valid;

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
public class EventController {
    private static final Logger LOG = LoggerFactory.getLogger(EventController.class);
    private final EventApplicationService eventApplicationService;

    public EventController(EventApplicationService eventApplicationService) {
        this.eventApplicationService = eventApplicationService;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(path = "/events")
    public List<EventOutput> findEvents() {
        return eventApplicationService.findEvents();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(path = "/events")
    public EventOutput createEvent(@Valid @RequestBody CreateEventInput input) {
        return eventApplicationService.createEvent(input);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(path = "/events/{eventId}")
    public EventOutput findEvents(@PathVariable Integer eventId) {
        return eventApplicationService.findEvent(eventId);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(path = "/events/{eventId}")
    public void updateEvent(@PathVariable Integer eventId, @RequestBody UpdateEventInput input) {
        eventApplicationService.updateEvent(eventId, input);
    }

    @ExceptionHandler({ DuplicateEventNameException.class, InvalidEventDateException.class })
    protected ResponseEntity<Object> handleDuplicateUserEmailAddressException(Exception ex) {
        LOG.error(ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage(), ex);
        return new ResponseEntity<>(errorResponse, errorResponse.getStatus());
    }
}
