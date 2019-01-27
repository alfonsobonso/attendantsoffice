package org.attendantsoffice.eventmanager.event.assigment;

import org.attendantsoffice.eventmanager.DefaultStyle;
import org.attendantsoffice.eventmanager.common.list.EntityListOutput;
import org.immutables.value.Value;

/**
 * User assignment information, formatted for the API.
 */
@DefaultStyle
@Value.Immutable
public interface EventTeamUserAssignmentOutput {

	public Integer getEventTeamUserAssignmentId();

	public EntityListOutput getUser();

	/**
	 * @return applicable event - denormalised for easy searching. 
	 * The application is responsible for making sure the event id here, and the one linked to the event are consistent.
	 */
	public EntityListOutput getEvent();

	/**
	 * @return applicable event team.
	 * @see {@code #getEventId()}
	 */
	public EntityListOutput getEventTeam();

	public Assignment getAssignment();
	
	public AssignmentStatus getAssignmentStatus();

}
