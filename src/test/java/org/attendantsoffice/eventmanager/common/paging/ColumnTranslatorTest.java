package org.attendantsoffice.eventmanager.common.paging;

import static org.junit.Assert.assertEquals;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import com.google.common.collect.ImmutableMap;

/**
 * Test the {@code SortTranslator} class.
 */
public class ColumnTranslatorTest {

    private ColumnTranslator translator;

    @Before
    public void setUp() {
        Map<String, String> translatedNames = ImmutableMap.<String, String>builder()
                .put("same", "same")
                .put("different", "is.different")
                .build();
        translator = new ColumnTranslator(translatedNames);
    }

    @Test
    public void testExtractColumnName() {
        assertEquals("same", translator.extractColumnName("same"));
        assertEquals("is.different", translator.extractColumnName("different"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExtractColumnNameNoMatch() {
        translator.extractColumnName("what");
    }

    @Test
    public void testTranslateSort() {
        Sort sort = new Sort(Direction.ASC, "same");
        SortOutput sortOutput = translator.translateSort(sort);
        assertEquals(Direction.ASC, sortOutput.getDirection());
        assertEquals("same", sortOutput.getSortBy());

        sort = new Sort(Direction.DESC, "is.different");
        sortOutput = translator.translateSort(sort);
        assertEquals(Direction.DESC, sortOutput.getDirection());
        assertEquals("different", sortOutput.getSortBy());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslateSortInvalidColumn() {
        Sort sort = new Sort(Direction.ASC, "who?");
        translator.translateSort(sort);
    }
}
