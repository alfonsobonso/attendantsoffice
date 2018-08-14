
package org.attendantsoffice.eventmanager.user;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping(path = "/api/users")
    public List<UserEntity> findUsers() {
        return StreamSupport.stream(userRepository.findAll().spliterator(), false).collect(Collectors.toList());
    }

    @GetMapping(path = "/api/users/{userId}")
    public UserEntity findUser(@PathVariable Integer userId) {
        return userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("No User#" + userId
                + " found"));
    }
}
