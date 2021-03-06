package org.attendantsoffice.eventmanager.user.security;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Implementation of the {@code UserAuthenticationService} that stores the authenticated user tokens in memory.
 * The token is just a randomly generated UUID
 */
@Service
public class InMemoryUUIDUserAuthenticationService implements UserAuthenticationService {
    private final EventManagerUserDetailsService eventManagerUserDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final Map<String, EventManagerUser> authenticatedUsers;

    public InMemoryUUIDUserAuthenticationService(EventManagerUserDetailsService eventManagerUserDetailsService,
            PasswordEncoder passwordEncoder) {
        this.eventManagerUserDetailsService = eventManagerUserDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.authenticatedUsers = new ConcurrentHashMap<>();
    }

    @Override
    public Pair<String, EventManagerUser> login(String email, String password) {
        Optional<EventManagerUser> optionalUser = eventManagerUserDetailsService.findUserByEmail(email);

        // optional if/else waiting for java 9
        if (!optionalUser.isPresent()) {
            throw new UserNotFoundException(email);
        }

        EventManagerUser user = optionalUser.get();

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new WrongPasswordException(user.getUserId(), email);
        }

        // belt'n'braces - ensure we don't end up having them logged in twice.
        Set<String> existingTokens = authenticatedUsers.entrySet().stream()
                .filter(e -> e.getValue().equals(user))
                .map(e -> e.getKey())
                .collect(Collectors.toSet());
        existingTokens.forEach(authenticatedUsers::remove);

        // we can now store the auth token
        final String token = UUID.randomUUID().toString();

        authenticatedUsers.put(token, user);

        return ImmutablePair.of(token, user);
    }

    @Override
    public Optional<EventManagerUser> findByToken(String token) {
        return Optional.ofNullable(authenticatedUsers.get(token));
    }

    @Override
    public void logout(Integer userId) {
        Set<String> existingTokens = authenticatedUsers.entrySet().stream()
                .filter(e -> e.getValue().getUserId().equals(userId))
                .map(e -> e.getKey())
                .collect(Collectors.toSet());
        existingTokens.forEach(authenticatedUsers::remove);
    }

}
