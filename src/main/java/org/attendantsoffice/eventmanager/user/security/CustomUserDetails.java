package org.attendantsoffice.eventmanager.user.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

/**
 * Extend the basic {@code UserDetails}, adding the userId.
 */
public class CustomUserDetails extends User {
    private static final long serialVersionUID = 1L;
    private final Integer userId;

    public CustomUserDetails(Integer userId, String username, String password,
            Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.userId = userId;
    }

    public Integer getUserId() {
        return userId;
    }


}
