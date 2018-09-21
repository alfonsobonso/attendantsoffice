package org.attendantsoffice.eventmanager.common.paging;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;

/**
 * Translate the underlying sort result back into the output name, if required.
 */
public class ColumnTranslator {
    private Map<String, String> translatedNames;
    private Map<String, String> inverseTranslatedNames;

    /**
     * @param translatedNames the mapping of search field name to column name
     */
    public ColumnTranslator(Map<String, String> translatedNames) {
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
