package org.attendantsoffice.eventmanager.event;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * Test the {@code EventApplicationService} class.
 */
@RunWith(MockitoJUnitRunner.class)
public class EventApplicationServiceTest {

    @Mock
    private EventRepository eventRepository;

    @Mock
    private EventMapper eventMapper;

    private EventApplicationService service;

    @Before
    public void setUp() {
        service = new EventApplicationService(eventRepository, eventMapper);
    }

    @Test
    public void testFindEvents() {
        EventEntity entity = eventEntity(1);

        when(eventRepository.findAllEvents()).thenReturn(Collections.singletonList(entity));

        EventOutput output = eventOutput(1);

        when(eventMapper.map(entity)).thenReturn(output);

        List<EventOutput> outputList = service.findEvents();
        assertEquals(1, outputList.size());

        verify(eventMapper, times(1)).map(any());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindEventNotFound() {
        EventEntity entity = eventEntity(1);

        when(eventRepository.findAllEvents()).thenReturn(Collections.singletonList(entity));

        service.findEvent(2);
    }

    @Test
    public void testFindEvent() {
        EventEntity entity = eventEntity(1);

        when(eventRepository.findAllEvents()).thenReturn(Collections.singletonList(entity));

        EventOutput output = eventOutput(1);
        when(eventMapper.map(entity)).thenReturn(output);

        EventOutput result = service.findEvent(1);
        assertEquals(output, result);

        verify(eventMapper, times(1)).map(any());
    }

    @Test
    public void testFindName() {
        EventEntity entity = eventEntity(1);

        when(eventRepository.findAllEvents()).thenReturn(Collections.singletonList(entity));
        String name = service.findName(1);
        assertEquals("Event#1", name);
    }

    private EventEntity eventEntity(int id) {
        EventEntity entity = new EventEntity();
        entity.setEventId(id);
        entity.setName("Event#" + id);
        return entity;
    }

    private ImmutableEventOutput eventOutput(int id) {
        ImmutableEventOutput output = ImmutableEventOutput.builder()
                .eventId(id)
                .name("Event#" + id)
                .location("location")
                .startDate(LocalDate.now())
                .endDate(LocalDate.now())
                .eventStatus(EventStatus.ANNOUNCED)
                .build();
        return output;
    }
}
