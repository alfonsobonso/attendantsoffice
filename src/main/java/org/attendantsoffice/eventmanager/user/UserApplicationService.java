package org.attendantsoffice.eventmanager.user;

import java.util.Map;
import java.util.Optional;

import org.attendantsoffice.eventmanager.common.paging.ColumnTranslator;
import org.attendantsoffice.eventmanager.common.paging.PageOutput;
import org.attendantsoffice.eventmanager.congregation.CongregationEntity;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.ImmutableMap;

@Service
@Transactional
public class UserApplicationService {
    private final ColumnTranslator columnTranslator;
    private final UserMapper userMapper;
    private final UserRepository userRepository;

    public UserApplicationService(UserMapper userMapper, UserRepository userRepository) {
        this.userMapper = userMapper;
        this.userRepository = userRepository;

        Map<String, String> translatedSortColumns = ImmutableMap.<String, String>builder().put("id", "userId")
                .put("firstName", "firstName")
                .put("lastName", "lastName")
                .put("homePhone", "homePhone")
                .put("mobilePhone", "mobilePhone")
                .put("email", "email")
                .put("congregation", "congregation.name")
                .build();
        columnTranslator = new ColumnTranslator(translatedSortColumns);
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

    public PageOutput<UserOutput> findUsers(UsersSearchCriteria searchCriteria) {
        Page<UserEntity> page = userRepository.findAll(searchCriteria, columnTranslator);
        PageOutput<UserOutput> output = PageOutput.of(page.map(userMapper::map), columnTranslator);

        return output;
    }

    public void updatePassword(Integer userId, String encodedPassword) {
        UserEntity entity = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("No User#" + userId + " found"));
        entity.setPassword(encodedPassword);
        userRepository.save(entity);
    }

    public void updateUser(Integer userId, UpdateUserInput input) {
        UserEntity entity = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("No User#" + userId + " found"));

        entity.setFirstName(input.getFirstName());
        entity.setLastName(input.getLastName());
        entity.setHomePhone(input.getHomePhone().orElse(null));
        entity.setMobilePhone(input.getMobilePhone().orElse(null));
        entity.setEmail(input.getEmail());

        CongregationEntity congregation = new CongregationEntity();
        congregation.setCongregationId(input.getCongregationId());
        entity.setCongregation(congregation);

        entity.setUserStatus(input.getUserStatus());
        entity.setPosition(input.getPosition());
        entity.setRole(input.getRole());

        userRepository.save(entity);
    }

}
