
package org.attendantsoffice.eventmanager.user;

import org.attendantsoffice.eventmanager.common.paging.PageOutput;
import org.attendantsoffice.eventmanager.mvc.error.ErrorResponse;
import org.attendantsoffice.eventmanager.user.security.WrongPasswordException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    @PostMapping(path = "/users/")
    public UserOutput createUser(@RequestBody CreateUserInput input) {
        return userApplicationService.createUser(input);
    }

    /**
     * Find the primary information about a specified user.
     * Note: this is the admin end point. Users accessing their own information will use the 'me' endpoint, with no
     * userId specified in the path.
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(path = "/users/{userId}")
    public UserOutput findUser(@PathVariable Integer userId) {
        return userApplicationService.findUser(userId);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(path = "/users/{userId}")
    public void updateUser(@PathVariable Integer userId, @RequestBody UpdateUserInput input) {
        userApplicationService.updateUser(userId, input);
    }

    @ExceptionHandler(DuplicateUserEmailAddressException.class)
    protected ResponseEntity<Object> handleDuplicateUserEmailAddressException(
            DuplicateUserEmailAddressException ex) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage(), ex);
        return new ResponseEntity<>(errorResponse, errorResponse.getStatus());
    }

}
