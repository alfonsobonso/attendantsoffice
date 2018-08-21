package org.attendantsoffice.eventmanager.user;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Sort;
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
        when(userRepository.findAll()).thenReturn(Lists.newArrayList(entity1, entity2));
        when(userMapper.map(entity1)).thenReturn(UserOutputTestDataBuilder.createUser(1));
        when(userMapper.map(entity2)).thenReturn(UserOutputTestDataBuilder.createUser(1));

        List<UserOutput> users = service.findUsers(Optional.empty(), Optional.empty());
        assertEquals(2, users.size());
        assertEquals(1, users.get(0).getUserId().intValue());
    }

    @Test
    public void testFindUsersIdSort() {
        UserEntity entity1 = user(1);
        UserEntity entity2 = user(1);
        when(userRepository.findAll(Sort.by(Direction.ASC, "userId"))).thenReturn(Lists.newArrayList(entity1, entity2));
        when(userMapper.map(entity1)).thenReturn(UserOutputTestDataBuilder.createUser(1));
        when(userMapper.map(entity2)).thenReturn(UserOutputTestDataBuilder.createUser(1));

        List<UserOutput> users = service.findUsers(Optional.of("id"), Optional.of(Direction.ASC));
        assertEquals(2, users.size());
        assertEquals(1, users.get(0).getUserId().intValue());
    }

    @Test
    public void testFindUsersFirstNameSort() {
        UserEntity entity1 = user(1);
        UserEntity entity2 = user(1);
        when(userRepository.findAll(Sort.by(Direction.DESC, "firstName"))).thenReturn(Lists.newArrayList(entity1,
                entity2));
        when(userMapper.map(entity1)).thenReturn(UserOutputTestDataBuilder.createUser(1));
        when(userMapper.map(entity2)).thenReturn(UserOutputTestDataBuilder.createUser(1));

        List<UserOutput> users = service.findUsers(Optional.of("firstName"), Optional.of(Direction.DESC));
        assertEquals(2, users.size());
        assertEquals(1, users.get(0).getUserId().intValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindUsersUnexpectedSort() {
        service.findUsers(Optional.of("unknown"), Optional.of(Direction.ASC));
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
