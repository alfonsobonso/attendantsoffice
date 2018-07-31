
package org.attendantsoffice.eventmanager.authentication;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface AuthenticationTokenRepository extends CrudRepository<AuthenticationTokenEntity, Long> {

    @Transactional(readOnly = true)
    public Optional<AuthenticationTokenEntity> findByToken(String token);

}