package org.attendantsoffice.eventmanager.common.paging;

import org.attendantsoffice.eventmanager.DefaultStyle;
import org.immutables.value.Value;
import org.immutables.value.Value.Parameter;
import org.springframework.data.domain.Sort.Direction;

/**
 * Define the paged result sort direction
 */
@DefaultStyle
@Value.Immutable
public interface SortOutput {
    /**
     * @return direction of the sort
     */
    @Parameter
    Direction getDirection();

    /**
     * @return the field which was sorted on. We only support a single column
     */
    @Parameter
    String getSortBy();
}
