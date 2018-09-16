package org.attendantsoffice.eventmanager.event;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EventController {
    private final EventApplicationService eventApplicationService;

    public EventController(EventApplicationService eventApplicationService) {
        this.eventApplicationService = eventApplicationService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(path = "/events")
    public List<EventOutput> findEvents() {
        return eventApplicationService.findEvents();
    }
}
