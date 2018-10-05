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

    @Transactional(readOnly = true)
    public EventOutput findEvent(Integer eventId) {
        List<EventEntity> entityList = eventRepository.findAllEvents();
        EventEntity entity = entityList.stream()
                .filter(c -> c.getEventId().equals(eventId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown Event#" + eventId));
        return eventMapper.map(entity);
    }

    @Transactional(readOnly = true)
    public String findName(Integer eventId) {
        List<EventEntity> events = eventRepository.findAllEvents();

        EventEntity entity = events.stream()
                .filter(c -> c.getEventId().equals(eventId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown Event#" + eventId));
        return entity.getName();
    }

}
