package org.attendantsoffice.eventmanager.event;

import java.time.LocalDate;

import org.attendantsoffice.eventmanager.DefaultStyle;
import org.immutables.value.Value;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * Event information, formatted for the API.
 */
@DefaultStyle
@Value.Immutable
public interface EventOutput {
    Integer getEventId();

    String getName();

    String getLocation();

    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate getStartDate();

    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate getEndDate();

    EventStatus getEventStatus();

    /**
     * @return indicate this is the current event, so will be used for the default view or sub-data, such as teams.
     * The application is responsible for ensuring we have one current event.
     */
    boolean isCurrent();
}
