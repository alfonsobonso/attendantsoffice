package org.attendantsoffice.eventmanager.event.team;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.Arrays;
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
    public void testMapNoParent() {
        // all attributes are mandatory
        EventTeamEntity entity = entity(1, 10, null);

        when(eventApplicationService.findName(10)).thenReturn("Event#10");

        EventTeamOutput output = mapper.map(entity, Collections.singletonList(entity));

        assertEquals(1, output.getEventTeamId().intValue());
        assertEquals("EventTeam#1", output.getName());
        assertEquals("EventTeam#1 (Billy Bob)", output.getNameWithCaptain());

        assertEquals(10, output.getEvent().getId().intValue());
        assertEquals("Event#10", output.getEvent().getName());
        assertFalse(output.getParentEventTeam().isPresent());
    }

    @Test
    public void testMapWithParent() {
        // all attributes are mandatory
        EventTeamEntity entity = entity(1, 10, null);
        EventTeamEntity entity2 = entity(2, 10, entity);

        when(eventApplicationService.findName(10)).thenReturn("Event#10");

        EventTeamOutput output = mapper.map(entity2, Arrays.asList(entity, entity2));

        assertEquals(2, output.getEventTeamId().intValue());
        assertEquals("EventTeam#2", output.getName());
        assertEquals("EventTeam#2 (Billy Bob)", output.getNameWithCaptain());

        assertEquals(10, output.getEvent().getId().intValue());
        assertEquals("Event#10", output.getEvent().getName());

        assertEquals(1, output.getParentEventTeam().get().getId().intValue());
        assertEquals("EventTeam#1 (Billy Bob)", output.getParentEventTeam().get().getName());
    }

    private EventTeamEntity entity(int id, int eventId, EventTeamEntity parent) {
        EventTeamEntity entity = new EventTeamEntity();
        entity.setEventTeamId(id);

        EventEntity event = new EventEntity();
        event.setEventId(eventId);

        entity.setEvent(event);
        entity.setCreatedByUserId(0);
        entity.setCreatedDateTime(Instant.now());
        entity.setUpdatedByUserId(1);
        entity.setUpdatedDateTime(Instant.now());
        entity.setName("EventTeam#" + id);
        entity.setNameWithCaptain("EventTeam#" + id + " (Billy Bob)");

        if (parent != null) {
            entity.setParentEventTeamId(parent.getEventTeamId());
        }

        return entity;
    }

}
