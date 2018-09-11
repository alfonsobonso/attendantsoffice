package org.attendantsoffice.eventmanager.user;

import static java.util.Optional.ofNullable;
import static org.attendantsoffice.eventmanager.user.QUserEntity.userEntity;

import java.util.Optional;

import org.attendantsoffice.eventmanager.common.paging.ColumnTranslator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.core.BooleanBuilder;

@Repository
@Transactional
public interface UserRepository extends PagingAndSortingRepository<UserEntity, Integer>,
        QuerydslPredicateExecutor<UserEntity> {
    int DEFAULT_PAGE_SIZE = 25;

    @Transactional(readOnly = true)
    default Page<UserEntity> findAll(UsersSearchCriteria searchCriteria, ColumnTranslator columnTranslator) {
        String sortColumn = columnTranslator.extractColumnName(ofNullable(searchCriteria.getSortBy()).orElse("id"));

        Pageable pageable = PageRequest.of(ofNullable(searchCriteria.getPage()).orElse(0),
                ofNullable(searchCriteria.getPageSize()).orElse(DEFAULT_PAGE_SIZE),
                ofNullable(searchCriteria.getSortDirection()).orElse(Direction.ASC),
                sortColumn);

        BooleanBuilder queryBuilder = new BooleanBuilder();
        if (searchCriteria.getFirstName() != null) {
            queryBuilder.and(userEntity.firstName.containsIgnoreCase(searchCriteria.getFirstName()));
        }
        if (searchCriteria.getLastName() != null) {
            queryBuilder.and(userEntity.lastName.containsIgnoreCase(searchCriteria.getLastName()));
        }
        if (searchCriteria.getHomePhone() != null) {
            queryBuilder.and(userEntity.homePhone.containsIgnoreCase(searchCriteria.getHomePhone()));
        }
        if (searchCriteria.getMobilePhone() != null) {
            queryBuilder.and(userEntity.mobilePhone.containsIgnoreCase(searchCriteria.getMobilePhone()));
        }
        if(searchCriteria.getEmail() != null) {
            queryBuilder.and(userEntity.email.containsIgnoreCase(searchCriteria.getEmail()));
        }
        if (searchCriteria.getUserStatus() != null) {
            queryBuilder.and(userEntity.userStatus.eq(searchCriteria.getUserStatus()));
        }
        if (searchCriteria.getCongregation() != null) {
            queryBuilder.and(userEntity.congregation.name.containsIgnoreCase(searchCriteria.getCongregation()));
        }

        Page<UserEntity> page = findAll(queryBuilder.getValue(), pageable);
        return page;
    }

    @Transactional(readOnly = true)
    Optional<UserEntity> findByEmail(String email);

}