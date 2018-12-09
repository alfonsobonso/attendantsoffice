package org.attendantsoffice.eventmanager.event;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
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

    public EventOutput createEvent(CreateEventInput input) {
        // some basic validation
        List<EventEntity> entityList = eventRepository.findAllEvents();

        // make sure we don't have an event with the same name already
        Optional<EventEntity> matchingEvent = entityList.stream()
                .filter(e -> e.getName().equalsIgnoreCase(input.getName().trim()))
                .findAny();
        if (matchingEvent.isPresent()) {
            throw new DuplicateEventNameException(matchingEvent.get().getEventId(), matchingEvent.get().getName());
        }

        // make sure the dates are sensible. Give it a bit if slack for the actual checks
        assertInputDatesValid(input.getStartDate(), input.getEndDate());

        EventEntity entity = new EventEntity();
        entity.setName(input.getName());
        entity.setLocation(input.getLocation());
        entity.setStartDate(input.getStartDate());
        entity.setEndDate(input.getEndDate());
        entity.setEventStatus(EventStatus.ANNOUNCED);

        eventRepository.saveEvent(entity);

        EventOutput output = eventMapper.map(entity);
        return output;
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

    public void updateEvent(Integer eventId, UpdateEventInput input) {
        List<EventEntity> entityList = eventRepository.findAllEvents();
        EventEntity entity = entityList.stream()
                .filter(c -> c.getEventId().equals(eventId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown Event#" + eventId));

        // some basic validation
        // make sure we don't have a different event with the same name already
        Optional<EventEntity> matchingEvent = entityList.stream()
                .filter(e -> e.getName().equalsIgnoreCase(input.getName().trim()))
                .filter(e -> !e.getEventId().equals(eventId))
                .findAny();
        if (matchingEvent.isPresent()) {
            throw new DuplicateEventNameException(matchingEvent.get().getEventId(), matchingEvent.get().getName());
        }

        // make sure the dates are sensible. Give it a bit if slack for the actual checks
        assertInputDatesValid(input.getStartDate(), input.getEndDate());

        entity.setName(input.getName());
        entity.setLocation(input.getLocation());
        entity.setStartDate(input.getStartDate());
        entity.setEndDate(input.getEndDate());
        entity.setEventStatus(input.getEventStatus());

        eventRepository.saveEvent(entity);
    }

    private void assertInputDatesValid(LocalDate startDate, LocalDate endDate) {
        long daysBetween = ChronoUnit.DAYS.between(startDate, endDate);
        if (daysBetween > 4 || daysBetween < 2) {
            throw new InvalidEventDateException(startDate, endDate, daysBetween);
        }
    }

}
