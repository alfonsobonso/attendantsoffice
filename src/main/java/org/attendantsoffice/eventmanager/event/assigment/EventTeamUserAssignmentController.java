/**
 * 
 */
package org.attendantsoffice.eventmanager.event.assigment;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EventTeamUserAssignmentController {
	private final EventTeamUserAssignmentApplicationService eventTeamUserAssignmentApplicationService;
	
	public EventTeamUserAssignmentController(
			EventTeamUserAssignmentApplicationService eventTeamUserAssignmentApplicationService) {
		this.eventTeamUserAssignmentApplicationService = eventTeamUserAssignmentApplicationService;
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(path = "/event-team-user-assignments")
    public List<EventTeamUserAssignmentOutput> findEventTeamUserAssignments(EventTeamUserAssignmentSearchCriteria searchCriteria) {
        return eventTeamUserAssignmentApplicationService.findEventTeamUserAssignments(searchCriteria);
    }
	
}
