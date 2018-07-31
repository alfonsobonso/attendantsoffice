package org.attendantsoffice.eventmanager.authentication;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.attendantsoffice.eventmanager.user.security.SecurityContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Control the creation and usage of an authentication token
 */
@Service
public class AuthenticationTokenApplicationService {
    private final AuthenticationTokenMailSender authenticationTokenMailSender;
    private final AuthenticationTokenRepository authenticationTokenRepository;
    private final Clock clock;
    private final int maxTokenAgeMins;

    public AuthenticationTokenApplicationService(AuthenticationTokenMailSender authenticationTokenMailSender,
            AuthenticationTokenRepository authenticationTokenRepository,
            Clock clock,
            @Value("${app.authentication.max-token-age-mins:1440}") int maxTokenAgeMins) {
        this.authenticationTokenMailSender = authenticationTokenMailSender;
        this.authenticationTokenRepository = authenticationTokenRepository;
        this.clock = clock;
        this.maxTokenAgeMins = maxTokenAgeMins;
    }

    /**
     * For a given userId with email address, store an authentication token and email the link to them.
     * The link will take them to a form that allows them to set their credentials.
     */
    public void sendAuthenticationTokenMail(Integer userId, String email) {
        String token = UUID.randomUUID().toString();

        AuthenticationTokenEntity entity = new AuthenticationTokenEntity();
        entity.setUserId(userId);
        entity.setToken(token);
        entity.setCreatedByUserId(SecurityContext.extractUserId());
        entity.setUpdatedByUserId(SecurityContext.extractUserId());

        authenticationTokenRepository.save(entity);
        authenticationTokenMailSender.mailAuthenticationTokenMail(userId, email, token);
    }

    /**
     * For a given token, look up the user id. If the token doesn't exist return Optional.empty.
     */
    public Optional<Integer> fetchAuthenticationTokenUserId(String token) throws AuthenticationTokenExpiredException,
            AuthenticationTokenUsedException {

        Optional<AuthenticationTokenEntity> tokenEntity = authenticationTokenRepository.findByToken(token);
        Optional<Integer> userId = tokenEntity.map(entity -> {
            if (entity.isUsed()) {
                throw new AuthenticationTokenUsedException(entity.getToken(), entity.getUserId());
            }
            int tokenAgeMins = calculateTokenAgeMins(entity.getCreatedDateTime());
            if (tokenAgeMins > maxTokenAgeMins) {
                throw new AuthenticationTokenExpiredException(entity.getToken(), entity.getUserId());
            }
            return entity.getUserId();
        });
        return userId;
    }

    public void markAuthenticationTokenUsed(String token) {
        Optional<AuthenticationTokenEntity> tokenEntity = authenticationTokenRepository.findByToken(token);
        if (!tokenEntity.isPresent()) {
            throw new IllegalStateException("Request to mark authenticaton token used failed - token [" + token
                    + " not found");
        }

        // not too worried here about whether is is already marked as used or is expired
        // if we have got here it will have recently passed checks for those.
        tokenEntity.ifPresent(entity -> {
            entity.setUsed(true);
            entity.setUpdatedByUserId(SecurityContext.extractUserId());
            authenticationTokenRepository.save(entity);
        });
    }

    private int calculateTokenAgeMins(Instant createdDateTime) {
        Long minutes = Duration.between(createdDateTime, clock.instant()).toMinutes();
        return minutes.intValue();
    }

}
