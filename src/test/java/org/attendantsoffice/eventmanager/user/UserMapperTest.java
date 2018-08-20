package org.attendantsoffice.eventmanager.user;

import static org.junit.Assert.assertEquals;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;

/**
 * Test the {@code UserMapper} class.
 */
public class UserMapperTest {

    private UserMapper mapper;

    @Before
    public void setUp() {
        mapper = new UserMapper();
    }

    @Test
    public void testMapMinimal() {
        UserEntity entity = createUserEntity();

        UserOutput output = mapper.map(entity);
        assertEquals(1, output.getUserId().intValue());
        assertEquals("first", output.getFirstName());
        assertEquals("last", output.getLastName());
        assertEquals("email@email.com", output.getEmail());
        assertEquals(100, output.getCongregation().getCongregationId().intValue());
        assertEquals("Cong#100", output.getCongregation().getName());
    }

    @Test
    public void testMapFullyPopulated() {
        UserEntity entity = createUserEntity();
        entity.setHomePhone("my home");
        entity.setMobilePhone("my mobile");

        UserOutput output = mapper.map(entity);
        assertEquals(1, output.getUserId().intValue());
        assertEquals("first", output.getFirstName());
        assertEquals("last", output.getLastName());
        assertEquals("email@email.com", output.getEmail());
        assertEquals(100, output.getCongregation().getCongregationId().intValue());
        assertEquals("Cong#100", output.getCongregation().getName());

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
        entity.setCongregationId(100);
        return entity;
    }

}
