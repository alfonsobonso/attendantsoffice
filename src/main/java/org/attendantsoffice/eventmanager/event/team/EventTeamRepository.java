package org.attendantsoffice.eventmanager.event.team;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface EventTeamRepository extends PagingAndSortingRepository<EventTeamEntity, Integer>,
        QuerydslPredicateExecutor<EventTeamEntity> {

    @Cacheable("eventTeams")
    default List<EventTeamEntity> findAllEventTeams() {
        List<EventTeamEntity> entityList = StreamSupport.stream(findAll().spliterator(), false)
                .collect(Collectors.toList());
        return entityList;
    }
}
