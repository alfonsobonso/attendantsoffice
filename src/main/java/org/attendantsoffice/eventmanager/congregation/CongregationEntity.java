package org.attendantsoffice.eventmanager.congregation;

import static javax.persistence.GenerationType.IDENTITY;

import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity(name = "congregation")
public class CongregationEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer congregationId;

    @NotNull
    @Size(max = 100)
    private String name;

    @Column(updatable = false)
    private Integer createdByUserId;

    @Column(updatable = false)
    private Instant createdDateTime;

    public Integer getCongregationId() {
        return congregationId;
    }

    public void setCongregationId(Integer congregationId) {
        this.congregationId = congregationId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

}
