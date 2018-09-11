
package org.attendantsoffice.eventmanager.user;

import org.attendantsoffice.eventmanager.common.paging.PageOutput;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    private final UserApplicationService userApplicationService;

    public UserController(UserApplicationService userApplicationService) {
        this.userApplicationService = userApplicationService;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(path = "/users")
    public PageOutput<UserOutput> findUsers(UsersSearchCriteria searchCriteria) {
        return userApplicationService.findUsers(searchCriteria);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(path = "/users/{userId}")
    public UserOutput findUser(@PathVariable Integer userId) {
        return userApplicationService.findUser(userId);
    }
}
