package org.attendantsoffice.eventmanager.event;

import org.springframework.stereotype.Component;

/**
 * Map between the underlying EventEntity and the EventOutput instance
 *
 */
@Component
public class EventMapper {

    public EventOutput map(EventEntity entity) {
        EventOutput output = ImmutableEventOutput.builder()
                .eventId(entity.getEventId())
                .name(entity.getName())
                .location(entity.getLocation())
                .startDate(entity.getStartDate())
                .endDate(entity.getEndDate())
                .eventStatus(entity.getEventStatus())
                .build();
        return output;
    }

}
