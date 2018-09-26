package org.attendantsoffice.eventmanager.congregation;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.List;

import org.attendantsoffice.eventmanager.common.list.EntityListOutput;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.google.common.collect.Lists;

/**
 * Test the {@code CongregationController} class
 */
@RunWith(MockitoJUnitRunner.class)
public class CongregationControllerTest {

    @Mock
    private CongregationApplicationService congregationApplicationService;

    private CongregationController controller;

    @Before
    public void setUp() {
        controller = new CongregationController(congregationApplicationService);
    }

    @Test
    public void testFetchList() {
        when(congregationApplicationService.findAll()).thenReturn(Lists.newArrayList(
                congregation(100, "name c"), congregation(101, "name a"), congregation(102, "name b")));
        List<EntityListOutput> result = controller.fetchList();
        assertEquals(3, result.size());
        // alphabetical
        assertEquals("name a", result.get(0).getName());
        assertEquals(101, result.get(0).getId().intValue());

        assertEquals("name c", result.get(2).getName());
        assertEquals(100, result.get(2).getId().intValue());
    }

    private CongregationEntity congregation(int id, String name) {
        CongregationEntity entity = new CongregationEntity();
        entity.setCongregationId(id);
        entity.setName(name);
        return entity;
    }

}
