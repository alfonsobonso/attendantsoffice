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
    public void testFindUsers() {
        UserEntity entity1 = user(1);
        UserEntity entity2 = user(1);
        when(userRepository.findAll()).thenReturn(Lists.newArrayList(entity1, entity2));
        when(userMapper.map(entity1)).thenReturn(UserOutputTestDataBuilder.createUser(1));
        when(userMapper.map(entity2)).thenReturn(UserOutputTestDataBuilder.createUser(1));

        List<UserOutput> users = service.findUsers();
        assertEquals(2, users.size());
        assertEquals(1, users.get(0).getUserId().intValue());
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

    private UserEntity user(int userId) {
        UserEntity entity = new UserEntity();
        entity.setUserId(userId);
        return entity;
    }
}
