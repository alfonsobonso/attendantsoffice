package org.attendantsoffice.eventmanager.event.team;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.List;

import org.attendantsoffice.eventmanager.common.list.ImmutableEntityListOutput;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * Test the {@code EventTeamController} class.
 */
@RunWith(MockitoJUnitRunner.class)
public class EventTeamControllerTest {

    @Mock
    private EventTeamApplicationService eventTeamApplicationService;

    private EventTeamController controller;

    @Captor
    private ArgumentCaptor<EventTeamSearchCriteria> eventTeamSearchCriteriaCaptor;

    @Before
    public void setUp() {
        controller = new EventTeamController(eventTeamApplicationService);
    }

    @Test
    public void testFindEventTeam() {
        EventTeamSearchCriteria criteria = new EventTeamSearchCriteria();
        criteria.setEventTeamId(200);

        EventTeamOutput output = output(200, 100);

        when(eventTeamApplicationService.findEventTeams(eventTeamSearchCriteriaCaptor.capture())).thenReturn(
                singletonList(output));
        EventTeamOutput result = controller.findEventTeam(200);

        assertEquals(200, result.getEventTeamId().intValue());
        assertEquals(200, eventTeamSearchCriteriaCaptor.getValue().getEventTeamId().intValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindEventTeamNotFound() {
        EventTeamSearchCriteria criteria = new EventTeamSearchCriteria();
        criteria.setEventTeamId(200);

        when(eventTeamApplicationService.findEventTeams(eventTeamSearchCriteriaCaptor.capture())).thenReturn(
                emptyList());
        controller.findEventTeam(200);
    }

    @Test
    public void testFindEventTeams() {
        EventTeamSearchCriteria criteria = new EventTeamSearchCriteria();
        criteria.setEventId(100);

        EventTeamOutput output = output(150, 100);

        when(eventTeamApplicationService.findEventTeams(eventTeamSearchCriteriaCaptor.capture())).thenReturn(
                singletonList(output));

        List<EventTeamOutput> eventTeams = controller.findEventTeams(100);

        assertEquals(1, eventTeams.size());
        assertEquals(100, eventTeamSearchCriteriaCaptor.getValue().getEventId().intValue());
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
