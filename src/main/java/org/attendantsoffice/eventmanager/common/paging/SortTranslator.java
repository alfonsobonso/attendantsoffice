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

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;

/**
 * Translate the underlying sort result back into the output name, if required.
 */
public class SortTranslator {
    private Map<String, String> translatedNames;
    private Map<String, String> inverseTranslatedNames;

    /**
     * @param translatedNames the mapping of search field name to column name
     */
    public SortTranslator(Map<String, String> translatedNames) {
        this.translatedNames =  translatedNames;
        this.inverseTranslatedNames = translatedNames.entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));
    }

    /**
     * From the incoming search field, return the database column name
     * @throws IllegalArgumentException if the field name is not mapped
     */
    public String extractColumnName(String searchFieldName) {
        return Optional.ofNullable(translatedNames.get(searchFieldName)).orElseThrow(() -> new IllegalArgumentException(
                "Unsupported sort field [" + searchFieldName + "]"));
    }

    /**
     * Convert the JPA sort into the sort output, translating the column name back into the search field.
     */
    public SortOutput translateSort(Sort sort) {
        Order order = sort.iterator().next();
        String property = order.getProperty();
        String translated = Optional.ofNullable(inverseTranslatedNames.get(property)).orElseThrow(
                () -> new IllegalArgumentException("Unsupported sort column [" + property + "]"));

        return ImmutableSortOutput.of(order.getDirection(), translated);
    }

}
