package org.attendantsoffice.eventmanager.user;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.attendantsoffice.eventmanager.congregation.CongregationApplicationService;
import org.attendantsoffice.eventmanager.congregation.CongregationEntity;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * Test the {@code UserMapper} class.
 */
@RunWith(MockitoJUnitRunner.class)
public class UserMapperTest {

    @Mock
    private CongregationApplicationService congregationApplicationService;

    private UserMapper mapper;

    @Before
    public void setUp() {
        mapper = new UserMapper(congregationApplicationService);
    }

    @Test
    public void testMapMinimal() {
        UserEntity entity = createUserEntity();

        when(congregationApplicationService.findName(100)).thenReturn("my cong");

        UserOutput output = mapper.map(entity);
        assertEquals(1, output.getUserId().intValue());
        assertEquals("first", output.getFirstName());
        assertEquals("last", output.getLastName());
        assertEquals("email@email.com", output.getEmail());
        assertEquals(100, output.getCongregation().getId().intValue());
        assertEquals("my cong", output.getCongregation().getName());
    }

    @Test
    public void testMapFullyPopulated() {
        UserEntity entity = createUserEntity();
        entity.setHomePhone("my home");
        entity.setMobilePhone("my mobile");

        when(congregationApplicationService.findName(100)).thenReturn("my cong");

        UserOutput output = mapper.map(entity);
        assertEquals(1, output.getUserId().intValue());
        assertEquals("first", output.getFirstName());
        assertEquals("last", output.getLastName());
        assertEquals("email@email.com", output.getEmail());
        assertEquals(100, output.getCongregation().getId().intValue());
        assertEquals("my cong", output.getCongregation().getName());

        assertEquals(Optional.of("my home"), output.getHomePhone());
        assertEquals(Optional.of("my mobile"), output.getMobilePhone());
    }

    private UserEntity createUserEntity() {
        // there are other non-null annotated fields, but not yet used
        UserEntity entity = new UserEntity();
        entity.setUserId(1);
        entity.setFirstName("first");
        entity.setLastName("last");
        entity.setEmail("email@email.com");
        entity.setUserStatus(UserStatus.UNAVAILABLE);
        entity.setPosition(UserPosition.ELDER);
        entity.setRole(UserRole.USER);

        CongregationEntity congregationEntity = new CongregationEntity();
        congregationEntity.setCongregationId(100);
        entity.setCongregation(congregationEntity);
        return entity;
    }

}
