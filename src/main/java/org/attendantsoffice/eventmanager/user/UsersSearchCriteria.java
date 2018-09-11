package org.attendantsoffice.eventmanager.user;

import javax.annotation.Nullable;

import org.springframework.data.domain.Sort.Direction;

/**
 * Criteria used to sort/filter/page users.
 */
public class UsersSearchCriteria {
    @Nullable
    private Integer page;

    @Nullable
    private Integer pageSize;

    @Nullable
    private String sortBy;

    @Nullable
    private Direction sortDirection;

    /**
     * @return page of results, zero-based, defaulting to 0
     */
    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    /**
     * @return page size, defaulting to 25
     */
    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    /**
     * @return sort column name
     */
    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    /**
     * @return sort direction, defaulting to ASC
     */
    public Direction getSortDirection() {
        return sortDirection;
    }

    public void setSortDirection(Direction sortDirection) {
        this.sortDirection = sortDirection;
    }

}
