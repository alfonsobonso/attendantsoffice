/*
 * Copyright (c) 2018 by Hotspring Ventures Limited
 * 16 Charles Ii Street (c/o Calder & Co), London SW1Y 4NW
 * All rights reserved.
 * This software is the confidential and proprietary information
 * of Hotspring Ventures Limited ("Confidential Information").
 * You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement
 * you entered into with Hotspring Ventures Limited.
 */
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


    static <T> PageOutput<T> of(Page<T> page, SortTranslator sortTranslator) {
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
