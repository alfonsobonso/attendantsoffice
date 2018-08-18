package org.attendantsoffice.eventmanager.user.security;

import java.util.Collections;
import java.util.Optional;

import org.attendantsoffice.eventmanager.user.UserEntity;
import org.attendantsoffice.eventmanager.user.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Spring security user details service, using the User information.
 * This works with our {@code EventManagerUserDetails}
 */
@Service
public class EventManagerUserDetailsService {
    private final UserRepository userRepository;

    public EventManagerUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<EventManagerUser> findUserByEmail(String email) throws UsernameNotFoundException {
        Optional<UserEntity> userEntity = userRepository.findByEmail(email);
        Optional<EventManagerUser> userDetails = userEntity.map(entity -> {
            if (entity.getPassword() == null) {
                throw new PasswordNotSetAuthenticationException(entity.getUserId(), entity.getEmail());
            }

            GrantedAuthority grantedAuthority = new SimpleGrantedAuthority("ROLE_" + entity.getRole().name());

            // note: we populate the CustomUserDetails, which allows us to access the UserId in the SecurityContext.
            EventManagerUser mapped = new EventManagerUser(entity.getUserId(), entity.getFirstName(),
                    entity.getLastName(), email, entity.getPassword(), Collections.singleton(grantedAuthority));
            return mapped;
        });

        return userDetails;
    }

}
