package org.attendantsoffice.eventmanager;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.immutables.value.Value.Style;

/**
 *
 * Immutables style override. We keep this as basic as possible
 */
@Retention(RetentionPolicy.CLASS)
@Style(optionalAcceptNullable = true, strictBuilder = true, get = { "is*", "get*" }, forceJacksonPropertyNames = false)
public @interface DefaultStyle {

}
