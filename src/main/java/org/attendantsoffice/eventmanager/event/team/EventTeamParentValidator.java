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
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

/**
 * Validate the submitted event team parent is valid.
 * It must be in the same event and can't be the same as the team itself or one of it's children
 */
@Component
public class EventTeamParentValidator {

    /**
     * @param eventTeam proposed event team
     * @param allEventTeams all currently persisted teams across all events
     */
    public void assertEventTeamParentValid(EventTeamEntity eventTeam, List<EventTeamEntity> allEventTeams) {
        // if we are not requesting to have a parent, no further checks required
        if (eventTeam.getParentEventTeamId() == null) {
            return;
        }

        assertParentTeamInSameEvent(eventTeam, allEventTeams);
        assertParentIsNotAChildTeam(eventTeam, allEventTeams);
    }

    private void assertParentTeamInSameEvent(EventTeamEntity eventTeam, List<EventTeamEntity> allEventTeams) {
        Optional<EventTeamEntity> matchingParentTeam = allEventTeams.stream()
                .filter(team -> team.getEventTeamId().equals(eventTeam.getParentEventTeamId()))
                .findAny();
        if (!matchingParentTeam.isPresent()) {
            throw new IllegalArgumentException("Parent EventTeam#" + eventTeam.getParentEventTeamId()
                    + " does not exist");
        }
        if (!matchingParentTeam.get().getEvent().getEventId().equals(eventTeam.getEvent().getEventId())) {
            throw new IllegalArgumentException("Parent EventTeam#" + eventTeam.getParentEventTeamId()
                    + " is linked to Event#" + matchingParentTeam.get().getEvent().getEventId());
        }
    }

    private void assertParentIsNotAChildTeam(EventTeamEntity eventTeam, List<EventTeamEntity> allEventTeams) {
        // if this is a team that is already persisted (i.e. it has an id) it may already have children
        // so ensure the parent isn't one of them
        if (eventTeam.getEventTeamId() != null) {
            Set<Integer> childTeamIds = findAllChildTeamIds(eventTeam.getEventTeamId(), allEventTeams);
            if (childTeamIds.contains(eventTeam.getParentEventTeamId())) {
                throw new IllegalArgumentException("Parent EventTeam#" + eventTeam.getParentEventTeamId()
                        + " is already one of the children");
            }
        }
    }

    private Set<Integer> findAllChildTeamIds(Integer eventTeamId, List<EventTeamEntity> allEventTeams) {
        Set<Integer> completeAncestorTeamIds = new HashSet<>();
        completeAncestorTeamIds.add(eventTeamId);

        findAllChildTeamIds(Collections.singleton(eventTeamId), completeAncestorTeamIds, allEventTeams);

        return completeAncestorTeamIds;
    }

    /**
     * Recursively called method that looks up all the children of the specified parents
     * @param parentTeamIds all discovered immediate parents we want to find the children of
     * @param completeAncestorTeamIds the complete set. This is the final result, but captured to prevent looped
     * relationships causing infinite recursion.
     * @param allEventTeams complete set of teams to check
     */
    private void findAllChildTeamIds(Set<Integer> parentTeamIds, Set<Integer> completeAncestorTeamIds,
            List<EventTeamEntity> allEventTeams) {
        Set<Integer> childTeamIds = allEventTeams.stream()
                .filter(team -> team.getParentEventTeamId() != null)
                .filter(team -> parentTeamIds.contains(team.getParentEventTeamId()))
                .map(team -> team.getEventTeamId())
                .collect(Collectors.toSet());

        // to prevent recursive loops we limit the recursive search to teams we haven't already found
        childTeamIds.removeAll(completeAncestorTeamIds);

        if (!childTeamIds.isEmpty()) {
            completeAncestorTeamIds.addAll(childTeamIds);
            findAllChildTeamIds(childTeamIds, completeAncestorTeamIds, allEventTeams);
        }
    }
}
