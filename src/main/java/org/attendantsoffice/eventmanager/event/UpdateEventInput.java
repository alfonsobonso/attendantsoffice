package org.attendantsoffice.eventmanager.event;

import java.time.LocalDate;

import javax.validation.constraints.Size;

import org.attendantsoffice.eventmanager.DefaultStyle;
import org.immutables.value.Value;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * User information, formatted for the API. This is the very basic event information, with no attempt to create teams,
 * assignments, etc.
 */
@DefaultStyle
@Value.Immutable
@JsonDeserialize(as = ImmutableUpdateEventInput.class)
public interface UpdateEventInput {

    @Size(min = 2)
    String getName();

    @Size(min = 2)
    String getLocation();

    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate getStartDate();

    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate getEndDate();

    EventStatus getEventStatus();

    /**
     * @return indicate this is the current event, so will be used for the default view or sub-data, such as teams.
     * The application is responsible for ensuring we have one current event. For an update, the event can't be marked
     * as not current if it is currently the current one - instead, we must mark a different event as the current one.
     */
    @JsonProperty("current")
    boolean isCurrent();
}
