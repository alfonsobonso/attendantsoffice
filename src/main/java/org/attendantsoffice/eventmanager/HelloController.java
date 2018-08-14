package org.attendantsoffice.eventmanager;

import java.util.Date;
import java.util.Optional;

import org.attendantsoffice.eventmanager.user.security.EventManagerUser;
import org.attendantsoffice.eventmanager.user.security.SecurityContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Placeholder - just playing
 */
@RestController
public class HelloController {

    @GetMapping("/hello")
    public String hello() {
        Optional<EventManagerUser> userDetails = SecurityContext.extractAuthenticatedUser();
        return "Hello, " + userDetails.get().getUsername() + ": the time at the server is now " + new Date() + "\n";
    }
}
