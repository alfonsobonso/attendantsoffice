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
package org.attendantsoffice.eventmanager.event.team;

import java.util.Collections;

import org.attendantsoffice.eventmanager.event.EventEntity;
import org.junit.Test;

import com.google.common.collect.Lists;

/**
 * Test the {@code EventTeamParentValidator} class.
 */
public class EventTeamParentValidatorTest {

    private EventTeamParentValidator validator = new EventTeamParentValidator();

    @Test
    public void testNoParentDefined() {
        // not saved, no parent
        EventTeamEntity team = team(null, null, 1);

        validator.assertEventTeamParentValid(team, Collections.singletonList(team(20, null, 1)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParentNotInAllTeams() {
        // not saved, with parent
        EventTeamEntity team = team(null, 10, 1);

        validator.assertEventTeamParentValid(team, Collections.singletonList(team(20, null, 1)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParentNotInSameEvent() {
        // not saved, with parent
        EventTeamEntity team = team(null, 10, 1);

        validator.assertEventTeamParentValid(team, Collections.singletonList(team(10, null, 2)));
    }

    @Test
    public void testParentInSameEvent() {
        // not saved, with parent
        EventTeamEntity team = team(null, 10, 1);

        validator.assertEventTeamParentValid(team, Collections.singletonList(team(10, null, 1)));
    }

    @Test
    public void testParentNoChildren() {
        // saved, with parent, no children
        EventTeamEntity team = team(11, 10, 1);

        validator.assertEventTeamParentValid(team, Lists.newArrayList(
                team(10, null, 1),
                team(11, 10, 1) // itself
        ));
    }

    @Test
    public void testParentWithChildren() {
        // saved, with parent, with children
        EventTeamEntity team = team(11, 10, 1);

        validator.assertEventTeamParentValid(team, Lists.newArrayList(
                team(10, null, 1), // no parent
                team(11, 10, 1), // itself
                team(12, 11, 1), // direct child
                team(13, 11, 1), // direct child
                team(14, 13, 1) // child of child
        ));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParentSameAsItself() {
        // saved, with parent, with children
        EventTeamEntity team = team(11, 11, 1);

        validator.assertEventTeamParentValid(team, Lists.newArrayList(
                team(10, null, 1), // no parent
                team(11, 10, 1), // itself
                team(12, 11, 1), // direct child
                team(13, 11, 1), // direct child
                team(14, 13, 1) // child of child
        ));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParentSameAsImmediateChild() {
        // saved, with parent, with children
        EventTeamEntity team = team(11, 12, 1);

        validator.assertEventTeamParentValid(team, Lists.newArrayList(
                team(10, null, 1), // no parent
                team(11, 10, 1), // itself
                team(12, 11, 1), // direct child
                team(13, 11, 1), // direct child
                team(14, 13, 1) // child of child
        ));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParentSameAsGrandchild() {
        // saved, with parent, with children
        EventTeamEntity team = team(11, 14, 1);

        validator.assertEventTeamParentValid(team, Lists.newArrayList(
                team(10, null, 1), // no parent
                team(11, 10, 1), // itself
                team(12, 11, 1), // direct child
                team(13, 11, 1), // direct child
                team(14, 13, 1) // child of child
        ));
    }

    private EventTeamEntity team(Integer eventTeamId, Integer parentEventTeamId, Integer eventId) {
        EventTeamEntity entity = new EventTeamEntity();
        entity.setEventTeamId(eventTeamId);
        entity.setParentEventTeamId(parentEventTeamId);
        entity.setEvent(new EventEntity());
        entity.getEvent().setEventId(eventId);
        return entity;
    }
}
