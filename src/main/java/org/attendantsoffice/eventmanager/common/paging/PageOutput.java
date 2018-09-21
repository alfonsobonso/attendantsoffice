package org.attendantsoffice.eventmanager.common.paging;

import java.util.List;

import org.attendantsoffice.eventmanager.DefaultStyle;
import org.immutables.value.Value;
import org.springframework.data.domain.Page;

/**
 * A generic page response.
 * This is based on the spring data Page, but that class is a little leaky.
 */
@DefaultStyle
@Value.Immutable
public interface PageOutput<T> {

    /**
     * @return results in this page
     */
    List<T> getItems();


    /**
     * @return the sort column/direction
     */
    SortOutput getSort();

    /**
     * @return total number of items (across all pages)
     */
    long getTotalItems();

    /**
     * @return current page size
     */
    int getPageSize();

    /**
     * @return the current page number, zero based
     */
    int getPageNumber();

    /**
     * @return total number of pages available
     */
    int getTotalPages();


    static <T> PageOutput<T> of(Page<T> page, ColumnTranslator sortTranslator) {
        SortOutput sort = sortTranslator.translateSort(page.getSort());

        @SuppressWarnings("unchecked")
        ImmutablePageOutput<T> output = (ImmutablePageOutput<T>) ImmutablePageOutput.builder()
                .addAllItems(page.getContent())
                .sort(sort)
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .totalPages(page.getTotalPages())
                .totalItems(page.getTotalElements())
                .build();
        return output;
    }

}
