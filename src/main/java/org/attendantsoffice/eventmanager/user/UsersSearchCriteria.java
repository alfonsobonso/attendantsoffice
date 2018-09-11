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

    @Nullable
    private String firstName;

    @Nullable
    private String lastName;

    @Nullable
    private String homePhone;

    @Nullable
    private String mobilePhone;

    @Nullable
    private String email;

    @Nullable
    private UserStatus userStatus;

    @Nullable
    private String congregation;

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

    /**
     * @return filter - first name contains
     */
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * @return filter - last name contains
     */
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * @return filter - home phone contains
     */
    public String getHomePhone() {
        return homePhone;
    }

    public void setHomePhone(String homePhone) {
        this.homePhone = homePhone;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    /**
     * @return filter - mobile phone contains
     */
    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    /**
     * @return filter - email contains
     */
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return filter - user status name contains
     */
    public UserStatus getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(UserStatus userStatus) {
        this.userStatus = userStatus;
    }

    /**
     * @return filter - congregation name contains
     */
    public String getCongregation() {
        return congregation;
    }

    public void setCongregation(String congregation) {
        this.congregation = congregation;
    }

}
