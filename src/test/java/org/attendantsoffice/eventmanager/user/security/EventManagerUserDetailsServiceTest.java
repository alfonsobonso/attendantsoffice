package org.attendantsoffice.eventmanager.user.security;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.attendantsoffice.eventmanager.user.UserEntity;
import org.attendantsoffice.eventmanager.user.UserRepository;
import org.attendantsoffice.eventmanager.user.UserRole;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * Test the {@code EventManagerUserDetailsService} class.
 */
@RunWith(MockitoJUnitRunner.class)
public class EventManagerUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    private EventManagerUserDetailsService service;

    @Before
    public void setUp() {
        service = new EventManagerUserDetailsService(userRepository);
    }

    @Test
    public void testFindUserByEmailNotFound() {
        String email = "email@example.com";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        Optional<EventManagerUser> result = service.findUserByEmail(email);
        assertFalse(result.isPresent());
    }

    @Test(expected = PasswordNotSetAuthenticationException.class)
    public void testFindUserByEmailNoPassword() {
        String email = "email@example.com";

        UserEntity entity = new UserEntity();

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(entity));

        service.findUserByEmail(email);
    }

    @Test
    public void testFindUserByEmailValid() {
        String email = "email@example.com";

        UserEntity entity = new UserEntity();
        entity.setUserId(1);
        entity.setPassword("pass");
        entity.setRole(UserRole.ADMIN);
        entity.setFirstName("first");
        entity.setLastName("last");

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(entity));

        Optional<EventManagerUser> result = service.findUserByEmail(email);
        assertTrue(result.isPresent());

        EventManagerUser user = result.get();
        assertEquals(1, user.getUserId().intValue());
        assertEquals("pass", user.getPassword());
        assertEquals("[ADMIN]", user.getAuthorities().toString());
        assertEquals("first", user.getFirstName());
        assertEquals("last", user.getLastName());


    }
}
