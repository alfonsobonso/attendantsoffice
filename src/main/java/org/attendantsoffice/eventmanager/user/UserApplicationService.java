package org.attendantsoffice.eventmanager.user;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserApplicationService {
    private final UserMapper userMapper;
    private final UserRepository userRepository;

    public UserApplicationService(UserMapper userMapper, UserRepository userRepository) {
        this.userMapper = userMapper;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public UserOutput findUser(Integer userId) {
        UserEntity entity = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("No User#" + userId + " found"));

        UserOutput output = userMapper.map(entity);
        return output;
    }

    public Optional<UserEntity> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public List<UserOutput> findUsers() {
        List<UserEntity> entityList = StreamSupport.stream(userRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
        List<UserOutput> outputList = entityList.stream().map(userMapper::map).collect(Collectors.toList());
        return outputList;
    }

    public void updatePassword(Integer userId, String encodedPassword) {
        UserEntity entity = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("No User#" + userId + " found"));
        entity.setPassword(encodedPassword);
        userRepository.save(entity);
    }

}
