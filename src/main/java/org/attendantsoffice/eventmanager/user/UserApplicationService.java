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

    public UserOutput createUser(CreateUserInput input) {
        // check for duplicate email address
        Optional<UserEntity> matchingEmailUser = userRepository.findByEmail(input.getEmail().trim());
        if (matchingEmailUser.isPresent()) {
            throw new DuplicateUserEmailAddressException(matchingEmailUser.get().getUserId(),
                    matchingEmailUser.get().getFirstName(), matchingEmailUser.get().getLastName(),
                    matchingEmailUser.get().getEmail());
        }

        UserEntity entity = new UserEntity();
        entity.setFirstName(input.getFirstName().trim());
        entity.setLastName(input.getLastName().trim());
        entity.setHomePhone(input.getHomePhone().map(String::trim).orElse(null));
        entity.setMobilePhone(input.getMobilePhone().map(String::trim).orElse(null));
        entity.setEmail(input.getEmail().trim());

        CongregationEntity congregation = new CongregationEntity();
        congregation.setCongregationId(input.getCongregationId());
        entity.setCongregation(congregation);
        entity.setPosition(input.getPosition());

        entity.setUserStatus(UserStatus.AVAILABLE); // active - may be requested to be approved by the cong.
        entity.setRole(UserRole.USER); // default user. Must be further edited to grant admin access

        userRepository.save(entity);

        UserOutput output = userMapper.map(entity);
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

        // if the email address has been changed, make sure this is not a duplicate
        if (!entity.getEmail().equalsIgnoreCase(input.getEmail().trim())) {
            Optional<UserEntity> matchingEmailUser = userRepository.findByEmail(input.getEmail().trim());
            if (matchingEmailUser.isPresent()) {
                throw new DuplicateUserEmailAddressException(matchingEmailUser.get().getUserId(),
                        matchingEmailUser.get().getFirstName(), matchingEmailUser.get().getLastName(),
                        matchingEmailUser.get().getEmail());
            }
        }

        entity.setFirstName(input.getFirstName().trim());
        entity.setLastName(input.getLastName().trim());
        entity.setHomePhone(input.getHomePhone().map(String::trim).orElse(null));
        entity.setMobilePhone(input.getMobilePhone().map(String::trim).orElse(null));
        entity.setEmail(input.getEmail().trim());

        CongregationEntity congregation = new CongregationEntity();
        congregation.setCongregationId(input.getCongregationId());
        entity.setCongregation(congregation);

        entity.setUserStatus(input.getUserStatus());
        entity.setPosition(input.getPosition());
        entity.setRole(input.getRole());

        userRepository.save(entity);
    }

}
