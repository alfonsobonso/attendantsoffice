
package org.attendantsoffice.eventmanager.user;

import java.util.Optional;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface UserRepository extends PagingAndSortingRepository<UserEntity, Integer> {

    @Transactional(readOnly = true)
    Optional<UserEntity> findByEmail(String email);

}