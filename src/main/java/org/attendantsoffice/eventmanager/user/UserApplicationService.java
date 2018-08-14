package org.attendantsoffice.eventmanager.user;

import java.util.Optional;

import org.springframework.stereotype.Service;

@Service
public class UserApplicationService {
    private final UserRepository userRepository;

    public UserApplicationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserEntity findUser(Integer userId) {
        return userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("No User#" + userId
                + " found"));
    }

    public Optional<UserEntity> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public void updatePassword(Integer userId, String encodedPassword) {
        UserEntity user = findUser(userId);
        user.setPassword(encodedPassword);
    }

}
