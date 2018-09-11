/*
 * Copyright (c) 2018 by Hotspring Ventures Limited
 * 16 Charles Ii Street (c/o Calder & Co), London SW1Y 4NW
 * All rights reserved.
 * This software is the confidential and proprietary information
 * of Hotspring Ventures Limited ("Confidential Information").
 * You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement
 * you entered into with Hotspring Ventures Limited.
 */
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
        EventEntity entity = new EventEntity();
        entity.setEventId(1);

        when(eventRepository.findAllEvents()).thenReturn(Collections.singletonList(entity));

        EventOutput output = ImmutableEventOutput.builder()
                .eventId(entity.getEventId())
                .name("Event#" + entity.getEventId())
                .location("location")
                .startDate(LocalDate.now())
                .endDate(LocalDate.now())
                .eventStatus(EventStatus.ANNOUNCED)
                .build();

        when(eventMapper.map(entity)).thenReturn(output);

        List<EventOutput> outputList = service.findEvents();
        assertEquals(1, outputList.size());

        verify(eventMapper, times(1)).map(any());
    }

}
