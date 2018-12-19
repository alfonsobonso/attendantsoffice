package org.attendantsoffice.eventmanager.event.team;

import static javax.persistence.GenerationType.IDENTITY;

import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.attendantsoffice.eventmanager.event.EventEntity;
import org.attendantsoffice.eventmanager.user.security.SecurityContext;

/**
 * Definition of a team defined for a specific event.
 * If the teams structure is unchanged from a previous event, a new set of event teams are created, linked to the new
 * event.
 */
@Entity(name = "event_team")
public class EventTeamEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer eventTeamId;

    @NotNull
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    private EventEntity event;

    @NotNull
    @Size(max = 100)
    private String name;

    @NotNull
    @Size(max = 500)
    private String nameWithCaptain;

    private Integer parentEventTeamId;

    @Column(updatable = false)
    private Integer createdByUserId;

    @Column(updatable = false)
    private Instant createdDateTime;

    private Integer updatedByUserId;
    private Instant updatedDateTime;

    @PrePersist
    private void prePersist() {
        createdDateTime = Instant.now();
        updatedDateTime = Instant.now();
        createdByUserId = SecurityContext.extractUserId();
        updatedByUserId = SecurityContext.extractUserId();
    }

    @PreUpdate
    private void preUpdate() {
        updatedDateTime = Instant.now();
        updatedByUserId = SecurityContext.extractUserId();
    }

    public Integer getEventTeamId() {
        return eventTeamId;
    }

    public void setEventTeamId(Integer eventTeamId) {
        this.eventTeamId = eventTeamId;
    }

    public EventEntity getEvent() {
        return event;
    }

    public void setEvent(EventEntity event) {
        this.event = event;
    }

    /**
     * @return explicitly entered name, e.g "Attendants HQ"
     * @see {@code #getNameWithCaptain()}
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return derived name. For convenience, we create the derived name from the team name and the assigned captain,
     * since this is how it is often searched for and displayed. The application will need to contain the logic of
     * making sure team name change, captain name changes or captain assignment changes are reflected here.
     */
    public String getNameWithCaptain() {
        return nameWithCaptain;
    }

    public void setNameWithCaptain(String nameWithCaptain) {
        this.nameWithCaptain = nameWithCaptain;
    }

    /**
     * @return optional team hierarchy.
     */
    public Integer getParentEventTeamId() {
        return parentEventTeamId;
    }

    public void setParentEventTeamId(Integer parentEventTeamId) {
        this.parentEventTeamId = parentEventTeamId;
    }

    public Integer getCreatedByUserId() {
        return createdByUserId;
    }

    public void setCreatedByUserId(Integer createdByUserId) {
        this.createdByUserId = createdByUserId;
    }

    public Instant getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(Instant createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public Integer getUpdatedByUserId() {
        return updatedByUserId;
    }

    public void setUpdatedByUserId(Integer updatedByUserId) {
        this.updatedByUserId = updatedByUserId;
    }

    public Instant getUpdatedDateTime() {
        return updatedDateTime;
    }

    public void setUpdatedDateTime(Instant updatedDateTime) {
        this.updatedDateTime = updatedDateTime;
    }

}
