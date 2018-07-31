package org.attendantsoffice.eventmanager.authentication;

import static javax.persistence.GenerationType.IDENTITY;

import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Define a token (UUID) that allows the user to set up their authentication - create or change their password.
 * We only expect this to be used once, so we capture the status and generation timestamp, allowing us to tell the user
 * if they have left it too long, or have already used it.
 */
@Entity(name = "authentication_token")
public class AuthenticationTokenEntity {

    @Id
    @GeneratedValue(strategy=IDENTITY)
    private Long authenticationTokenId;

    @NotNull
    Integer userId;

    @NotNull
    @Size(min = 36, max = 36)
    private String token;

    private boolean used;

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
    }

    @PreUpdate
    private void preUpdate() {
        updatedDateTime = Instant.now();
    }

    public Long getAuthenticationTokenId() {
        return authenticationTokenId;
    }

    public void setAuthenticationTokenId(Long authenticationTokenId) {
        this.authenticationTokenId = authenticationTokenId;
    }

    /**
     * @return user the token is linked to.
     */
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * @return unique (UUID) token, sent to the user in a secure way, allowing them to set their password.
     */
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    /**
     * @return indicate the token has been accessed to access the set password page, so should not be reused.
     */
    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    /**
     * @return the authenticated user that created the token. If this is created by an unauthenticated user, this will
     * by the system id. It might be the token is created by an admin giving an unauthenticated user a helping hand (or
     * on user creation).
     */
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

    /**
     * @return the user that updated the token. This is likely to be when it is used.
     */
    public Integer getUpdatedByUserId() {
        return updatedByUserId;
    }

    public void setUpdatedByUserId(Integer updatedByUserId) {
        this.updatedByUserId = updatedByUserId;
    }

    /**
     * @return the time the token was updated. This is likely to be when it is used.
     */
    public Instant getUpdatedDateTime() {
        return updatedDateTime;
    }

    public void setUpdatedDateTime(Instant updatedDateTime) {
        this.updatedDateTime = updatedDateTime;
    }

}
