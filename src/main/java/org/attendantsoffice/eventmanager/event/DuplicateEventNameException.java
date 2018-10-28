package org.attendantsoffice.eventmanager.event;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Indicate a new event, or an existing one changing the name is clashing with another one.
 */
@ResponseStatus(code = HttpStatus.UNPROCESSABLE_ENTITY)
public class DuplicateEventNameException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public DuplicateEventNameException(Integer eventId, String name) {
        super("Duplicate event name. " + name + " matches existing Event#" + eventId);
    }

}
