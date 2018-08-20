package org.attendantsoffice.eventmanager.authentication;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Clock;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * Test the {@code AuthenticationTokenApplicationService} class.
 */
@RunWith(MockitoJUnitRunner.class)
public class AuthenticationTokenApplicationServiceTest {

    @Mock
    AuthenticationTokenMailSender authenticationTokenMailSender;

    @Mock
    AuthenticationTokenRepository authenticationTokenRepository;

    @Captor
    private ArgumentCaptor<AuthenticationTokenEntity> authenticationTokenEntityCaptor;

    private AuthenticationTokenApplicationService applicationService;
    private Clock clock;
    int maxTokenAgeMins = 30;

    @Before
    public void setUp() {
        clock = Clock.systemUTC();
        applicationService = new AuthenticationTokenApplicationService(authenticationTokenMailSender,
                authenticationTokenRepository, clock, maxTokenAgeMins);
    }

    @Test
    public void testMailAuthenticationTokenMail() {
        int userId = 100;
        String email = "test@example.com";

        when(authenticationTokenRepository.save(authenticationTokenEntityCaptor.capture())).thenReturn(null);

        applicationService.sendAuthenticationTokenMail(userId, email);

        AuthenticationTokenEntity authenticationTokenEntity = authenticationTokenEntityCaptor.getValue();
        assertEquals(0, authenticationTokenEntity.getCreatedByUserId().intValue());
        assertNotNull(authenticationTokenEntity.getToken());

        verify(authenticationTokenMailSender, times(1)).mailAuthenticationTokenMail(userId, email,
                authenticationTokenEntity.getToken());
    }

    @Test
    public void testFetchAuthenticationTokenUserIdNotAuthenticated() {
        String token = "mytoken";

        when(authenticationTokenRepository.findByToken(token)).thenReturn(Optional.empty());

        Optional<Integer> userId = applicationService.fetchAuthenticationTokenUserId(token);
        assertEquals(Optional.empty(), userId);
    }

    @Test(expected = AuthenticationTokenUsedException.class)
    public void testFetchAuthenticationTokenUserIdUsed() {
        String token = "mytoken";

        AuthenticationTokenEntity entity = new AuthenticationTokenEntity();
        entity.setUsed(true);

        when(authenticationTokenRepository.findByToken(token)).thenReturn(Optional.of(entity));

        Optional<Integer> userId = applicationService.fetchAuthenticationTokenUserId(token);
        assertEquals(Optional.empty(), userId);
    }

    @Test(expected = AuthenticationTokenExpiredException.class)
    public void testFetchAuthenticationTokenUserIdExpired() {
        String token = "mytoken";

        AuthenticationTokenEntity entity = new AuthenticationTokenEntity();
        entity.setCreatedDateTime(clock.instant().minus(maxTokenAgeMins + 1, ChronoUnit.MINUTES));

        when(authenticationTokenRepository.findByToken(token)).thenReturn(Optional.of(entity));

        Optional<Integer> userId = applicationService.fetchAuthenticationTokenUserId(token);
        assertEquals(Optional.empty(), userId);
    }

    @Test
    public void testFetchAuthenticationTokenUserIdValid() {
        String token = "mytoken";

        AuthenticationTokenEntity entity = new AuthenticationTokenEntity();
        entity.setCreatedDateTime(clock.instant().minus(maxTokenAgeMins - 1, ChronoUnit.MINUTES));
        entity.setUserId(100);

        when(authenticationTokenRepository.findByToken(token)).thenReturn(Optional.of(entity));

        Optional<Integer> userId = applicationService.fetchAuthenticationTokenUserId(token);
        assertEquals(Optional.of(100), userId);
    }

    @Test(expected = IllegalStateException.class)
    public void testMarkAuthenticationTokenUsedNotFound() {
        String token = "mytoken";

        when(authenticationTokenRepository.findByToken(token)).thenReturn(Optional.empty());

        applicationService.markAuthenticationTokenUsed(token);
    }

    @Test
    public void testMarkAuthenticationTokenUsed() {
        String token = "mytoken";

        AuthenticationTokenEntity entity = new AuthenticationTokenEntity();

        when(authenticationTokenRepository.findByToken(token)).thenReturn(Optional.of(entity));

        applicationService.markAuthenticationTokenUsed(token);

        verify(authenticationTokenRepository).save(authenticationTokenEntityCaptor.capture());

        assertTrue(authenticationTokenEntityCaptor.getValue().isUsed());
    }

}
