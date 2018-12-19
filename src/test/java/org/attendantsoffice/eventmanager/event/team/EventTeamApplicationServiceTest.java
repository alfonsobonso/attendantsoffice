package org.attendantsoffice.eventmanager.event.team;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.attendantsoffice.eventmanager.common.list.ImmutableEntityListOutput;
import org.attendantsoffice.eventmanager.event.EventEntity;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.google.common.collect.Lists;

/**
 * Test the {@code EventTeamApplicationService} class.
 */
@RunWith(MockitoJUnitRunner.class)
public class EventTeamApplicationServiceTest {
    @Mock
    private EventTeamRepository eventTeamRepository;

    @Mock
    private EventTeamMapper eventTeamMapper;

    @Mock
    private EventTeamParentValidator eventTeamParentValidator;

    private EventTeamApplicationService service;

    @Before
    public void setUp() {
        service = new EventTeamApplicationService(eventTeamRepository, eventTeamMapper, eventTeamParentValidator);
    }

    @Test
    public void findEventTeamsByTeam() {
        EventTeamSearchCriteria searchCriteria = new EventTeamSearchCriteria();
        searchCriteria.setEventTeamId(11);

        EventTeamEntity team10 = eventTeamEntity(10, 2, null);
        EventTeamEntity team11 = eventTeamEntity(11, 2, 10);
        List<EventTeamEntity> teams = Lists.newArrayList(team10, team11);

        when(eventTeamRepository.findAllEventTeams()).thenReturn(teams);
        when(eventTeamMapper.map(team11, teams)).thenReturn(output(11, 2));

        List<EventTeamOutput> outputs = service.findEventTeams(searchCriteria);
        assertEquals(1, outputs.size());

        assertEquals(11, outputs.get(0).getEventTeamId().intValue());

        verify(eventTeamMapper, times(1)).map(any(), any());
    }

    @Test
    public void findEventTeamsByEvent() {
        EventTeamSearchCriteria searchCriteria = new EventTeamSearchCriteria();
        searchCriteria.setEventId(2);

        EventTeamEntity team10 = eventTeamEntity(10, 2, null);
        EventTeamEntity team11 = eventTeamEntity(11, 2, 10);
        EventTeamEntity team12 = eventTeamEntity(12, 3, null); // filtered out
        List<EventTeamEntity> teams = Lists.newArrayList(team10, team11, team12);

        when(eventTeamRepository.findAllEventTeams()).thenReturn(teams);
        when(eventTeamMapper.map(team10, teams)).thenReturn(output(10, 2));
        when(eventTeamMapper.map(team11, teams)).thenReturn(output(11, 2));

        List<EventTeamOutput> outputs = service.findEventTeams(searchCriteria);
        assertEquals(2, outputs.size());

        assertEquals(10, outputs.get(0).getEventTeamId().intValue());

        verify(eventTeamMapper, times(2)).map(any(), any());
    }

    @Test
    public void testCreateEventTeam() {
        EventTeamEntity team = eventTeamEntity(10, 2, null);
        when(eventTeamRepository.findAllEventTeams()).thenReturn(Lists.newArrayList(team));

        service.createEventTeam(2, ImmutableCreateEventTeamInput.builder().name("EventTeam#1").build());

        verify(eventTeamParentValidator, times(1)).assertEventTeamParentValid(any(), any());
        verify(eventTeamRepository, times(1)).saveEventTeam(any());
    }

    @Test(expected = DuplicateEventTeamNameException.class)
    public void testCreateEventTeamDuplicateName() {
        EventTeamEntity team = eventTeamEntity(10, 2, null);
        when(eventTeamRepository.findAllEventTeams()).thenReturn(Lists.newArrayList(team));

        service.createEventTeam(2, ImmutableCreateEventTeamInput.builder().name("EventTeam#10").build());

        verify(eventTeamParentValidator, never()).assertEventTeamParentValid(any(), any());
        verify(eventTeamRepository, never()).saveEventTeam(any());
    }

    @Test
    public void findEventName() {
        EventTeamEntity team = eventTeamEntity(10, 2, null);
        when(eventTeamRepository.findAllEventTeams()).thenReturn(Lists.newArrayList(team));
        String name = service.findName(10);

        assertEquals("EventTeam#10", name);
    }

    private EventTeamEntity eventTeamEntity(int id, int eventId, Integer parentEventTeamId) {
        EventTeamEntity entity = new EventTeamEntity();
        entity.setEventTeamId(id);

        EventEntity event = new EventEntity();
        event.setEventId(eventId);
        entity.setEvent(event);
        entity.setName("EventTeam#" + id);
        entity.setParentEventTeamId(parentEventTeamId);
        return entity;
    }

    private ImmutableEventTeamOutput output(int id, int eventId) {
        ImmutableEventTeamOutput output = ImmutableEventTeamOutput.builder()
                .eventTeamId(id)
                .event(ImmutableEntityListOutput.of(eventId, "Event#" + eventId))
                .name("EventTeam#1")
                .nameWithCaptain("EventTeam#1 (Jimmy Jones)")
                .build();
        return output;
    }

}
