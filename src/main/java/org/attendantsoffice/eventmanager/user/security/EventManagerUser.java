package org.attendantsoffice.eventmanager.user.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

/**
 * Extend the basic {@code User}, adding the userId.
 */
public class EventManagerUser extends User {
    private static final long serialVersionUID = 1L;
    private final String firstName;
    private final String lastName;
    private final Integer userId;

    public EventManagerUser(Integer userId, String firstName, String lastName, String email, String password,
            Collection<? extends GrantedAuthority> authorities) {
        super(email, password, authorities);
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Integer getUserId() {
        return userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

}
