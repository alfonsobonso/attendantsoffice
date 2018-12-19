
package org.attendantsoffice.eventmanager.event;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
@CacheConfig(cacheNames = "events")
public interface EventRepository extends CrudRepository<EventEntity, Integer> {

    @Cacheable
    default List<EventEntity> findAllEvents() {
        List<EventEntity> entityList = StreamSupport.stream(findAll().spliterator(), false)
                .collect(Collectors.toList());
        return entityList;
    }

    /**
     * Save the event and clear the list cache
     */
    @CacheEvict(allEntries = true)
    default EventEntity saveEvent(EventEntity eventEntity) {
        return save(eventEntity);
    }
}