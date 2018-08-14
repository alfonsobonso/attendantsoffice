
package org.attendantsoffice.eventmanager.user;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface UserRepository extends CrudRepository<UserEntity, Integer> {

    @Transactional(readOnly = true)
    Optional<UserEntity> findByEmail(String email);

}