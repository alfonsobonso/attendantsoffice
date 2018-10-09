package org.attendantsoffice.eventmanager.event.team;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.Collections;

import org.attendantsoffice.eventmanager.event.EventApplicationService;
import org.attendantsoffice.eventmanager.event.EventEntity;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * Test the {@code EventTeamMapper} class.
 */
@RunWith(MockitoJUnitRunner.class)
public class EventTeamMapperTest {

    @Mock
    private EventApplicationService eventApplicationService;

    private EventTeamMapper mapper;

    @Before
    public void setUp() {
        mapper = new EventTeamMapper(eventApplicationService);
    }

    @Test
    public void testMap() {
        // all attributes are mandatory
        EventTeamEntity entity = new EventTeamEntity();
        entity.setEventTeamId(1);

        // event will have a minimal join
        EventEntity event = new EventEntity();
        event.setEventId(10);

        entity.setEvent(event);
        entity.setCreatedByUserId(0);
        entity.setCreatedDateTime(Instant.now());
        entity.setUpdatedByUserId(1);
        entity.setUpdatedDateTime(Instant.now());
        entity.setName("EventTeam#1");
        entity.setNameWithCaptain("EventTeam#1 (Billy Bob)");

        when(eventApplicationService.findName(10)).thenReturn("Event#10");

        EventTeamOutput output = mapper.map(entity, Collections.singletonList(entity));

        assertEquals(1, output.getEventTeamId().intValue());
        assertEquals("EventTeam#1", output.getName());
        assertEquals("EventTeam#1 (Billy Bob)", output.getNameWithCaptain());

        assertEquals(10, output.getEvent().getId().intValue());
        assertEquals("Event#10", output.getEvent().getName());

    }

}
