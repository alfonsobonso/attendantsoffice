package org.attendantsoffice.eventmanager.congregation;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Collections;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * Test the {@code CongregationApplicationService} class.
 */
@RunWith(MockitoJUnitRunner.class)
public class CongregationApplicationServiceTest {

    @Mock
    private CongregationRepository congregationRepository;

    private CongregationApplicationService service;

    @Before
    public void setUp() {
        service = new CongregationApplicationService(congregationRepository);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindNameNoMatch() {
        when(congregationRepository.findAllCongregations()).thenReturn(Collections.emptyList());
        service.findName(1);
    }

    @Test
    public void testFindNameMatch() {
        CongregationEntity entity = new CongregationEntity();
        entity.setCongregationId(1);
        entity.setName("cong 100");
        when(congregationRepository.findAllCongregations()).thenReturn(Collections.singletonList(entity));
        String name = service.findName(1);

        assertEquals("cong 100", name);
    }
}
