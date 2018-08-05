package org.attendantsoffice.eventmanager.user.security;

import java.util.Optional;

import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Custom context, wrapping the SecurityContextHolder, allowing us to extract the user id from the
 * {@code CustomUserDetails}
 */
public class SecurityContext {

    /**
     * @return extract the currently authenticated user, empty if no authentication
     */
    public static Optional<CustomUserDetails> extractAuthenticatedUser() {
        if (SecurityContextHolder.getContext() == null || SecurityContextHolder.getContext()
                .getAuthentication() == null) {
            return Optional.empty();
        }
        CustomUserDetails user = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        return Optional.ofNullable(user);
    }

    /**
     * @return extract the currently authenticated user id, empty if no authentication
     */
    public static Optional<Integer> extractAuthenticatedUserId() {
        return extractAuthenticatedUser().map(CustomUserDetails::getUserId);
    };

    /**
     * @return extract the currently authenticated user id, falling back to the system id 0 if no user is authenticated.
     */
    public static Integer extractUserId() {
        return extractAuthenticatedUserId().orElse(0);
    }

}
