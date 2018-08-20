package org.attendantsoffice.eventmanager.authentication;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.Optional;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.attendantsoffice.eventmanager.user.UserApplicationService;
import org.attendantsoffice.eventmanager.user.UserOutputTestDataBuilder;
import org.attendantsoffice.eventmanager.user.security.EventManagerUser;
import org.attendantsoffice.eventmanager.user.security.UserAuthenticationService;
import org.attendantsoffice.eventmanager.user.security.UserNotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Test the {@code AuthenticationService}
 */
@RunWith(MockitoJUnitRunner.class)
public class AuthenticationServiceTest {

    @Mock
    AuthenticationTokenApplicationService authenticationTokenApplicationService;

    @Mock
    UserAuthenticationService userAuthenticationService;

    @Mock
    UserApplicationService userApplicationService;

    @Mock
    PasswordEncoder passwordEncoder;

    private AuthenticationService authenticationService;

    @Before
    public void setUp() {
        authenticationService = new AuthenticationService(authenticationTokenApplicationService,
                userAuthenticationService,
                userApplicationService,
                passwordEncoder);
    }

    @Test
    public void testLogin() {
        EventManagerUser user = new EventManagerUser(100, "first", "last", "first.last@example.com", "blah", Collections.emptyList());
        Pair<String, EventManagerUser> authServiceResult = ImmutablePair.of("mytoken", user);
        when(userAuthenticationService.login("myemail", "mypassword")).thenReturn(authServiceResult);
        Pair<String, EventManagerUser> loginResult = authenticationService.login("myemail", "mypassword");
        assertEquals("mytoken", loginResult.getLeft());

    }

    @Test(expected = UserNotFoundException.class)
    public void testSendAuthenticationTokenMailUserNotFound() {
        when(userApplicationService.findByEmail("myemail")).thenReturn(Optional.empty());
        authenticationService.sendAuthenticationTokenMail("myemail");
    }

    @Test
    public void testSendAuthenticationTokenMailUserFound() {
        when(userApplicationService.findByEmail("myemail")).thenReturn(
                Optional.of(UserOutputTestDataBuilder.createUser(100)));
        authenticationService.sendAuthenticationTokenMail("myemail");

        verify(authenticationTokenApplicationService, times(1)).sendAuthenticationTokenMail(100, "myemail");
    }

    @Test
    public void testFetchAuthenticationTokenUserId() {
        when(authenticationTokenApplicationService.fetchAuthenticationTokenUserId("mytoken"))
                .thenReturn(Optional.of(123));
        Optional<Integer> userId = authenticationService.fetchAuthenticationTokenUserId("mytoken");
        assertEquals(Optional.of(123), userId);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitAccessTokenPasswordNoUserMatch() {
        when(authenticationTokenApplicationService.fetchAuthenticationTokenUserId("mytoken"))
                .thenReturn(Optional.empty());
        authenticationService.submitAccessTokenPassword("mytoken", "mypassword");
    }

    @Test
    public void testSubmitAccessTokenPassword() {
        when(authenticationTokenApplicationService.fetchAuthenticationTokenUserId("mytoken"))
                .thenReturn(Optional.of(123));
        when(passwordEncoder.encode("mypassword")).thenReturn("myencodedpassword");
        authenticationService.submitAccessTokenPassword("mytoken", "mypassword");

        verify(authenticationTokenApplicationService).markAuthenticationTokenUsed("mytoken");

        verify(userApplicationService).updatePassword(123, "myencodedpassword");
        verify(userAuthenticationService).logout(123);
    }
}
