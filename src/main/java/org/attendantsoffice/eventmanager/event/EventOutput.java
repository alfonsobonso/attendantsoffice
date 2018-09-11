package org.attendantsoffice.eventmanager.event;

import java.time.LocalDate;

import org.attendantsoffice.eventmanager.DefaultStyle;
import org.immutables.value.Value;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * User information, formatted for the API.
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
}
