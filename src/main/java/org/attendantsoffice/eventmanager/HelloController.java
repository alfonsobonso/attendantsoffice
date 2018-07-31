package org.attendantsoffice.eventmanager;

import java.util.Date;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Placeholder - just playing
 */
@RestController
public class HelloController {

    @GetMapping("/api/hello")
    public String hello() {
        return "Hello, the time at the server is now " + new Date() + "\n";
    }

    @GetMapping("/api/test")
    public String hello(@RequestParam int value) {
        return "Hello, the time at the server is now " + new Date() + "\n";
    }
}
