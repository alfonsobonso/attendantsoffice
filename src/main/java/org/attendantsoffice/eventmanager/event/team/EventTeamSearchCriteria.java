package org.attendantsoffice.eventmanager.event.team;

import javax.annotation.Nullable;

/**
 * Criteria used when filtering event teams in the backend.
 * Generally the number of teams is small enough that we do the filtering in the UI, but we ma filter on the event since
 * that is the usual view.
 */
public class EventTeamSearchCriteria {
    @Nullable
    public Integer eventTeamId;

    @Nullable
    private Integer eventId;

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

}
