package org.attendantsoffice.eventmanager.event;

import static org.junit.Assert.assertEquals;

import java.time.Instant;
import java.time.LocalDate;
import java.time.Month;

import org.junit.Before;
import org.junit.Test;

/**
 * Test the {@code EventMapper} class.
 */
public class EventMapperTest {

    private EventMapper mapper;

    @Before
    public void setUp() {
        mapper = new EventMapper();
    }

    @Test
    public void testMap() {
        // all attributes are mandatory
        EventEntity entity = new EventEntity();
        entity.setEventId(1);
        entity.setCreatedByUserId(0);
        entity.setCreatedDateTime(Instant.now());
        entity.setUpdatedByUserId(1);
        entity.setUpdatedDateTime(Instant.now());
        entity.setEventStatus(EventStatus.COMPLETED);
        entity.setName("Event#1");
        entity.setStartDate(LocalDate.of(2018, Month.AUGUST, 1));
        entity.setEndDate(LocalDate.of(2018, Month.AUGUST, 3));
        entity.setLocation("event loc");
        EventOutput output = mapper.map(entity);

        assertEquals(1, output.getEventId().intValue());
        assertEquals(EventStatus.COMPLETED, output.getEventStatus());
        assertEquals("Event#1", output.getName());
        assertEquals("event loc", output.getLocation());
        assertEquals(LocalDate.of(2018, Month.AUGUST, 1), output.getStartDate());
        assertEquals(LocalDate.of(2018, Month.AUGUST, 3), output.getEndDate());
    }

}
