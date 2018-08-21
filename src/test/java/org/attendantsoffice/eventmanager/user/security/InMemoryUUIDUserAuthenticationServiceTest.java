package org.attendantsoffice.eventmanager.user.security;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.Optional;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Test the {@code InMemoryUUIDUserAuthenticationService} class.
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class InMemoryUUIDUserAuthenticationServiceTest {

    @Mock
    private EventManagerUserDetailsService eventManagerUserDetailsService;

    @Mock
    private PasswordEncoder passwordEncoder;

    private InMemoryUUIDUserAuthenticationService service;

    @Before
    public void setUp() {
        service = new InMemoryUUIDUserAuthenticationService(eventManagerUserDetailsService, passwordEncoder);
    }

    @Test(expected = UserNotFoundException.class)
    public void testLoginUserNotFound() {
        String email = "email@example.com";
        when(eventManagerUserDetailsService.findUserByEmail(email)).thenReturn(Optional.empty());
        service.login(email, "pass");
    }

    @Test(expected = WrongPasswordException.class)
    public void testLoginWrongPassword() {
        String email = "email@example.com";
        String password = "pass";
        EventManagerUser user = new EventManagerUser(1, "first", "last", email, "encodedPassword", Collections
                .emptySet());

        when(eventManagerUserDetailsService.findUserByEmail(email)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(password, "encodedPassword")).thenReturn(false);
        service.login(email, password);
    }

    @Test
    public void testLoginValidPassword() {
        String email = "email@example.com";
        String password = "pass";
        EventManagerUser user = new EventManagerUser(1, "first", "last", email, "encodedPassword", Collections
                .emptySet());

        when(eventManagerUserDetailsService.findUserByEmail(email)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(password, "encodedPassword")).thenReturn(true);
        Pair<String, EventManagerUser> result = service.login(email, password);

        assertEquals(user, result.getRight());
        assertEquals(36, result.getLeft().length()); // 36 chars = standard UUID length

        // while we are here, check we can fetch and remove the token
        String token = result.getLeft();

        Optional<EventManagerUser> fetchByTokenUser = service.findByToken(token);
        assertTrue(fetchByTokenUser.isPresent());
        assertEquals(user, fetchByTokenUser.get());

        service.logout(2); // not us
        fetchByTokenUser = service.findByToken(token);
        assertTrue(fetchByTokenUser.isPresent());

        service.logout(1); // us
        fetchByTokenUser = service.findByToken(token);
        assertFalse(fetchByTokenUser.isPresent());
    }

    public void testFindByTokenNotAutenticated() {
        Optional<EventManagerUser> user = service.findByToken("invalid");
        assertFalse(user.isPresent());
    }

}
