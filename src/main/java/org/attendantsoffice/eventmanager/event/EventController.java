package org.attendantsoffice.eventmanager.event;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EventController {
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
    @GetMapping(path = "/events/{eventId}")
    public EventOutput findEvents(@PathVariable Integer eventId) {
        return eventApplicationService.findEvent(eventId);
    }
}
