package org.attendantsoffice.eventmanager.event.assigment;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class EventTeamUserAssignmentApplicationService {

	List<EventTeamUserAssignmentOutput> findEventTeamUserAssignments(EventTeamUserAssignmentSearchCriteria searchCriteria) {
		// TODO
		return Collections.emptyList();
	}
}
