package org.attendantsoffice.eventmanager.authentication;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.attendantsoffice.eventmanager.user.UserApplicationService;
import org.attendantsoffice.eventmanager.user.security.UserAuthenticationService;
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
        when(userAuthenticationService.login("myemail", "mypassword")).thenReturn("mytoken");
        String token = authenticationService.login("myemail", "mypassword");
        assertEquals("mytoken", token);

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
