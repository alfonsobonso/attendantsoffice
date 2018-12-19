package org.attendantsoffice.eventmanager.event.team;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Indicate a new team, or an existing one changing its name, is clashing with another team in the same event.
 */
@ResponseStatus(code = HttpStatus.UNPROCESSABLE_ENTITY)
public class DuplicateEventTeamNameException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public DuplicateEventTeamNameException(Integer eventTeamId, String name) {
        super("Duplicate team name. " + name + " matches existing EventTeam#" + eventTeamId);
    }

}
