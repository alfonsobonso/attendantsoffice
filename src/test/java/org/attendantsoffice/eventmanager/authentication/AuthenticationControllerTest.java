/*
 * Copyright (c) 2018 by Hotspring Ventures Limited
 * 16 Charles Ii Street (c/o Calder & Co), London SW1Y 4NW
 * All rights reserved.
 * This software is the confidential and proprietary information
 * of Hotspring Ventures Limited ("Confidential Information").
 * You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement
 * you entered into with Hotspring Ventures Limited.
 */
package org.attendantsoffice.eventmanager.authentication;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.Optional;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.attendantsoffice.eventmanager.user.UserRole;
import org.attendantsoffice.eventmanager.user.security.EventManagerUser;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

/**
 * Test the {@code AuthenticationController} class.
 * Note, this is the functional test of the java class, not the mock annotated endpoints
 */
@RunWith(MockitoJUnitRunner.class)
public class AuthenticationControllerTest {

    @Mock
    private AuthenticationService authenticationService;

    private AuthenticationController controller;

    @Before
    public void setUp() {
        controller = new AuthenticationController(authenticationService);
    }

    @Test
    public void testLogin() {
        LoginInput input = ImmutableLoginInput.builder().email("test@example.com").password("test pass").build();

        EventManagerUser user = new EventManagerUser(100, "first", "last", "test@example.com", "secret password",
                Collections.singleton(new SimpleGrantedAuthority(UserRole.ADMIN.name())));

        Pair<String, EventManagerUser> result =   ImmutablePair.of("auth-uuid", user);

        when(authenticationService.login("test@example.com", "test pass")).thenReturn(result);

        LoginOutput output = controller.login(input);
        assertEquals("first", output.getFirstName());
        assertEquals("last", output.getLastName());
        assertEquals("ADMIN", output.getRole());
        assertEquals("auth-uuid", output.getToken());
        assertEquals(100, output.getUserId());
    }

    @Test
    public void testRequestAccessToken() {
        RequestAccessTokenInput input = ImmutableRequestAccessTokenInput.builder()
                .email("myemail@example.com ")
                .build();

        controller.requestAccessToken(input);

        verify(authenticationService, times(1)).sendAuthenticationTokenMail("myemail@example.com");
    }

    @Test
    public void testFetchAccessTokenStatusExpired() {
        String token = "my-token";

        when(authenticationService.fetchAuthenticationTokenUserId(token)).thenThrow(
                AuthenticationTokenExpiredException.class);

        AccessTokenStatusOutput output = controller.fetchAccessTokenStatus(token);
        assertEquals(AccessTokenStatusOutput.Status.EXPIRED, output.getStatus());
    }

    @Test
    public void testFetchAccessTokenStatusUsed() {
        String token = "my-token";

        when(authenticationService.fetchAuthenticationTokenUserId(token)).thenThrow(
                AuthenticationTokenUsedException.class);

        AccessTokenStatusOutput output = controller.fetchAccessTokenStatus(token);
        assertEquals(AccessTokenStatusOutput.Status.ALREADY_USED, output.getStatus());
    }

    @Test
    public void testFetchAccessTokenStatusUnrecognised() {
        String token = "my-token";

        when(authenticationService.fetchAuthenticationTokenUserId(token)).thenReturn(Optional.empty());

        AccessTokenStatusOutput output = controller.fetchAccessTokenStatus(token);
        assertEquals(AccessTokenStatusOutput.Status.UNRECOGNISED, output.getStatus());
    }

    @Test
    public void testFetchAccessTokenStatusValid() {
        String token = "my-token";

        when(authenticationService.fetchAuthenticationTokenUserId(token)).thenReturn(Optional.of(1));

        AccessTokenStatusOutput output = controller.fetchAccessTokenStatus(token);
        assertEquals(AccessTokenStatusOutput.Status.VALID, output.getStatus());
    }

    public void testSubmitAccessTokenPassword() {
        String token = "my-token";
        AccessTokenPasswordInput input = ImmutableAccessTokenPasswordInput.builder().password("top secret").build();

        controller.submitAccessTokenPassword(token, input);

        verify(authenticationService, times(1)).submitAccessTokenPassword(token, "top secret");
    }

}
