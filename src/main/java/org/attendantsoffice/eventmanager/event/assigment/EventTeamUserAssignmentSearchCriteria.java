/**
 * 
 */
package org.attendantsoffice.eventmanager.event.assigment;

import javax.annotation.Nullable;

public class EventTeamUserAssignmentSearchCriteria {

	@Nullable
	private Integer eventTeamId;
	
	@Nullable
	private Integer eventId;
	
	@Nullable
	private Integer userId;

	public Integer getEventTeamId() {
		return eventTeamId;
	}

	public void setEventTeamId(Integer eventTeamId) {
		this.eventTeamId = eventTeamId;
	}

	public Integer getEventId() {
		return eventId;
	}

	public void setEventId(Integer eventId) {
		this.eventId = eventId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	
}
