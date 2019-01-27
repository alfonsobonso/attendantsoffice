package org.attendantsoffice.eventmanager.event.assigment;

import static javax.persistence.GenerationType.IDENTITY;

import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import org.attendantsoffice.eventmanager.user.security.SecurityContext;

/**
 * Define the team assignment for a given user.
 */
public class EventTeamUserAssignmentEntity {

	@Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer eventTeamUserAssignmentId;
	
	@Column(nullable = false)
	private Integer userId;
	
	@Column(nullable = false)
	private Integer eventId;
	
	@Column(nullable = false)
	private Integer eventTeamId;
	
	@Convert(converter=AssignmentConverter.class)
	@Column(name="assignment_code", nullable = false)
	private Assignment assignment;

	@Convert(converter=AssignmentStatusConverter.class)
	@Column(name="assignment_status_code", nullable = false)
	private AssignmentStatus assignmentStatus;
	
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

	public Integer getEventTeamUserAssignmentId() {
		return eventTeamUserAssignmentId;
	}

	public void setEventTeamUserAssignmentId(Integer eventTeamUserAssignmentId) {
		this.eventTeamUserAssignmentId = eventTeamUserAssignmentId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	/**
	 * @return applicable event - denormalised for easy searching. 
	 * The application is responsible for making sure the event id here, and the one linked to the event are consistent.
	 */
	public Integer getEventId() {
		return eventId;
	}

	public void setEventId(Integer eventId) {
		this.eventId = eventId;
	}

	/**
	 * @return applicable event team.
	 * @see {@code #getEventId()}
	 */
	public Integer getEventTeamId() {
		return eventTeamId;
	}

	public void setEventTeamId(Integer eventTeamId) {
		this.eventTeamId = eventTeamId;
	}

	public Assignment getAssignment() {
		return assignment;
	}

	public void setAssignment(Assignment assignment) {
		this.assignment = assignment;
	}

	public AssignmentStatus getAssignmentStatus() {
		return assignmentStatus;
	}

	public void setAssignmentStatus(AssignmentStatus assignmentStatus) {
		this.assignmentStatus = assignmentStatus;
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
