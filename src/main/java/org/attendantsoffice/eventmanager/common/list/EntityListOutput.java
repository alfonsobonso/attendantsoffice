package org.attendantsoffice.eventmanager.common.list;

import org.attendantsoffice.eventmanager.DefaultStyle;
import org.immutables.value.Value;
import org.immutables.value.Value.Parameter;

/**
 * The basic name/value pairing used for basic lists and named identifiers, such as type-ahead fields.
 */
@DefaultStyle
@Value.Immutable
public interface EntityListOutput {

    @Parameter
    Integer getId();

    @Parameter
    String getName();
}
