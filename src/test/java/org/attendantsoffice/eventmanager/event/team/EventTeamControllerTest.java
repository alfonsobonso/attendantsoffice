package org.attendantsoffice.eventmanager.event.team;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Collections;
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
    public void testFindEventTeams() {
        EventTeamSearchCriteria criteria = new EventTeamSearchCriteria();
        criteria.setEventId(100);

        EventTeamOutput outputs = output(150, 100);

        when(eventTeamApplicationService.findEventTeams(eventTeamSearchCriteriaCaptor.capture())).thenReturn(Collections
                .singletonList(outputs));

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
