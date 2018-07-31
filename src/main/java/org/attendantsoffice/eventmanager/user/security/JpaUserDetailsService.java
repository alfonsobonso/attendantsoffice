package org.attendantsoffice.eventmanager.user.security;

import java.util.Collections;
import java.util.Optional;

import org.attendantsoffice.eventmanager.user.UserEntity;
import org.attendantsoffice.eventmanager.user.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Spring security user details service, using the User information
 *
 */
@Service
public class JpaUserDetailsService implements UserDetailsService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public JpaUserDetailsService(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<UserEntity> userEntity = userRepository.findByEmail(email);
        UserDetails userDetails = userEntity.map(entity -> {
            if (entity.getPassword() == null) {
                throw new PasswordNotSetAuthenticationException(entity.getUserId(), entity.getEmail());
            }

            GrantedAuthority grantedAuthority = new SimpleGrantedAuthority("ROLE_" + entity.getRole().name());

            // note: we populate the CustomUserDetails, which allows us to access the UserId in the SecurityContext.
            CustomUserDetails mapped = new CustomUserDetails(entity.getUserId(),
                    email,
                    passwordEncoder.encode(entity.getPassword()),
                    Collections.singleton(grantedAuthority));
            return mapped;
        }).orElseThrow(() -> new UsernameNotFoundException("No name matches [" + email + "]"));

        return userDetails;
    }

}
