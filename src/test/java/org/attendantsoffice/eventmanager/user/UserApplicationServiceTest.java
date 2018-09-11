package org.attendantsoffice.eventmanager.user;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.attendantsoffice.eventmanager.common.paging.PageOutput;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;

import com.google.common.collect.Lists;

/**
 * Test the {@code UserApplicationService} class.
 */
@RunWith(MockitoJUnitRunner.class)
public class UserApplicationServiceTest {

    @Mock
    private UserMapper userMapper;

    @Mock
    private UserRepository userRepository;

    private UserApplicationService service;

    @Captor
    private ArgumentCaptor<UserEntity> userEntityCaptor;

    @Before
    public void setUp() {
        service = new UserApplicationService(userMapper, userRepository);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindUserNotFound() {
        when(userRepository.findById(1)).thenReturn(Optional.empty());
        service.findUser(1);
    }

    @Test
    public void testFindUser() {
        UserEntity entity = user(1);
        when(userRepository.findById(1)).thenReturn(Optional.of(entity));
        when(userMapper.map(entity)).thenReturn(UserOutputTestDataBuilder.createUser(1));
        UserOutput output = service.findUser(1);

        assertEquals(1, output.getUserId().intValue());
    }

    @Test
    public void testFindUsersNoSort() {
        UserEntity entity1 = user(1);
        UserEntity entity2 = user(1);

        Pageable pageable = PageRequest.of(0, 25, Direction.ASC, "userId");

        UsersSearchCriteria criteria = new UsersSearchCriteria();

        when(userRepository.findAll(eq(criteria), any()))
                .thenReturn(new PageImpl<>(Lists.newArrayList(entity1, entity2), pageable, 2));
        when(userMapper.map(entity1)).thenReturn(UserOutputTestDataBuilder.createUser(1));
        when(userMapper.map(entity2)).thenReturn(UserOutputTestDataBuilder.createUser(1));

        PageOutput<UserOutput> page = service.findUsers(criteria);
        assertEquals(1, page.getTotalPages());
        assertEquals(Direction.ASC, page.getSort().getDirection());
        assertEquals("id", page.getSort().getSortBy());
        assertEquals(2, page.getItems().size());
        assertEquals(1, page.getItems().get(0).getUserId().intValue());
    }

    @Test
    public void testFindUsersIdSort() {
        UserEntity entity1 = user(1);
        UserEntity entity2 = user(1);

        Pageable pageable = PageRequest.of(0, 25, Direction.ASC, "userId");

        UsersSearchCriteria criteria = new UsersSearchCriteria();
        criteria.setSortBy("id");
        criteria.setSortDirection(Direction.ASC);

        when(userRepository.findAll(eq(criteria), any()))
                .thenReturn(new PageImpl<>(Lists.newArrayList(entity1, entity2), pageable, 2));
        when(userMapper.map(entity1)).thenReturn(UserOutputTestDataBuilder.createUser(1));
        when(userMapper.map(entity2)).thenReturn(UserOutputTestDataBuilder.createUser(1));

        PageOutput<UserOutput> page = service.findUsers(criteria);
        assertEquals(2, page.getItems().size());
        assertEquals(1, page.getItems().get(0).getUserId().intValue());
    }

    @Test
    public void testFindUsersFirstNameSort() {
        UserEntity entity1 = user(1);
        UserEntity entity2 = user(1);

        Pageable pageable = PageRequest.of(0, 25, Direction.DESC, "firstName");

        UsersSearchCriteria criteria = new UsersSearchCriteria();
        criteria.setSortBy("firstName");
        criteria.setSortDirection(Direction.DESC);

        when(userRepository.findAll(eq(criteria), any()))
                .thenReturn(new PageImpl<>(Lists.newArrayList(entity1, entity2), pageable, 2));
        when(userMapper.map(entity1)).thenReturn(UserOutputTestDataBuilder.createUser(1));
        when(userMapper.map(entity2)).thenReturn(UserOutputTestDataBuilder.createUser(1));

        PageOutput<UserOutput> page = service.findUsers(criteria);
        assertEquals(2, page.getItems().size());
        assertEquals(1, page.getItems().get(0).getUserId().intValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdatePasswordNotFound() {
        when(userRepository.findById(1)).thenReturn(Optional.empty());
        service.updatePassword(1, "encoded");
    }

    @Test
    public void testUpdatePassword() {
        UserEntity entity = user(1);
        when(userRepository.findById(1)).thenReturn(Optional.of(entity));
        service.updatePassword(1, "encoded");

        verify(userRepository, times(1)).save(userEntityCaptor.capture());

        assertEquals("encoded", userEntityCaptor.getValue().getPassword());
    }

    public void testFindByEmail() {
        String email = "email@email.com";
        UserEntity entity = user(1);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(entity));
        when(userMapper.map(entity)).thenReturn(UserOutputTestDataBuilder.createUser(1));
        Optional<UserOutput> output = service.findByEmail(email);

        assertEquals(1, output.get().getUserId().intValue());
    }

    private UserEntity user(int userId) {
        UserEntity entity = new UserEntity();
        entity.setUserId(userId);
        return entity;
    }
}
