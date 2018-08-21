package org.attendantsoffice.eventmanager.user;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
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

    public Optional<UserOutput> findByEmail(String email) {
        Optional<UserOutput> output = userRepository.findByEmail(email).map(userMapper::map);
        return output;
    }

    public List<UserOutput> findUsers(Optional<String> sortBy, Optional<Direction> sortDirection) {
        Iterable<UserEntity> iterable;
        if (sortBy.isPresent() && sortDirection.isPresent()) {
            String translatedSortBy;
            switch (sortBy.get()) {
                case "id":
                    translatedSortBy = "userId";
                    break;
                case "firstName":
                case "lastName":
                case "homePhone":
                case "mobilePhone":
                case "email":
                    translatedSortBy = sortBy.get();
                    break;
                default:
                    throw new IllegalArgumentException("Unsupported sort field [" + sortBy.get() + "]");
            }
            iterable = userRepository.findAll(Sort.by(sortDirection.get(), translatedSortBy));
        } else {
            iterable = userRepository.findAll();
        }

        List<UserEntity> entityList = StreamSupport.stream(iterable.spliterator(), false)
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
