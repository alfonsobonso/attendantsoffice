package org.attendantsoffice.eventmanager.user;

import static java.util.Optional.ofNullable;

import java.util.Map;
import java.util.Optional;

import org.attendantsoffice.eventmanager.common.paging.PageOutput;
import org.attendantsoffice.eventmanager.common.paging.SortTranslator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.ImmutableMap;

@Service
@Transactional
public class UserApplicationService {
    private static final int DEFAULT_PAGE_SIZE = 25;
    private final SortTranslator sortTranslator;
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
        sortTranslator = new SortTranslator(translatedSortColumns);
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
        String sortColumn = sortTranslator.extractColumnName(ofNullable(searchCriteria.getSortBy()).orElse("id"));

        Pageable pageable = PageRequest.of(ofNullable(searchCriteria.getPage()).orElse(0),
                ofNullable(searchCriteria.getPageSize()).orElse(DEFAULT_PAGE_SIZE),
                ofNullable(searchCriteria.getSortDirection()).orElse(Direction.ASC),
                sortColumn);
        Page<UserEntity> page = userRepository.findAll(pageable);
        PageOutput<UserOutput> output = PageOutput.of(page.map(userMapper::map), sortTranslator);

        return output;
    }

    public void updatePassword(Integer userId, String encodedPassword) {
        UserEntity entity = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("No User#" + userId + " found"));
        entity.setPassword(encodedPassword);
        userRepository.save(entity);
    }

}
