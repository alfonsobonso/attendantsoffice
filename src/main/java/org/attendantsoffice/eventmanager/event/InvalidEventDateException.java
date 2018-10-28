package org.attendantsoffice.eventmanager.event;

import java.time.LocalDate;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Indicate the event dates are unlikely
 */
@ResponseStatus(code = HttpStatus.UNPROCESSABLE_ENTITY)
public class InvalidEventDateException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public InvalidEventDateException(LocalDate startDate, LocalDate endDate, long daysBetween) {
        super("Days between " + startDate + " and " + endDate + " is " + daysBetween + " days");
    }

}
