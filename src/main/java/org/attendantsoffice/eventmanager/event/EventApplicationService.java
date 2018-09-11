package org.attendantsoffice.eventmanager.event;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Fetch/update the underlying event information.
 * We expect few entries here, so we cache this as a complete list
 */
@Service
@Transactional
public class EventApplicationService {
    private final EventRepository eventRepository;
    private final EventMapper eventMapper;

    public EventApplicationService(EventRepository eventRepository, EventMapper eventMapper) {
        this.eventRepository = eventRepository;
        this.eventMapper = eventMapper;
    }

    @Transactional(readOnly = true)
    public List<EventOutput> findEvents() {
        List<EventEntity> entityList = eventRepository.findAllEvents();
        List<EventOutput> outputList = entityList.stream().map(eventMapper::map).collect(Collectors.toList());
        return outputList;
    }

}
