
package org.attendantsoffice.eventmanager.user;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort.Direction;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    private final UserApplicationService userApplicationService;

    public UserController(UserApplicationService userApplicationService) {
        this.userApplicationService = userApplicationService;
    }

    @GetMapping(path = "/users")
    public List<UserOutput> findUsers(@RequestParam Optional<String> sortBy, Optional<Direction> sortDirection) {
        return userApplicationService.findUsers(sortBy, sortDirection);
    }

    @GetMapping(path = "/users/{userId}")
    public UserOutput findUser(@PathVariable Integer userId) {
        return userApplicationService.findUser(userId);
    }
}
