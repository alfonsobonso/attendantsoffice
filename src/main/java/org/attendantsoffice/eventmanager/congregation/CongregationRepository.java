
package org.attendantsoffice.eventmanager.congregation;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface CongregationRepository extends CrudRepository<CongregationEntity, Integer> {

    @Cacheable("congregations")
    default List<CongregationEntity> findAllCongregations() {
        List<CongregationEntity> entityList = StreamSupport.stream(findAll().spliterator(), false)
                .collect(Collectors.toList());
        return entityList;
    }

}