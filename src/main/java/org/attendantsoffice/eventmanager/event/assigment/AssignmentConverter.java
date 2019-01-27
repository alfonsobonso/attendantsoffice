/**
 * 
 */
package org.attendantsoffice.eventmanager.event.assigment;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class AssignmentConverter implements AttributeConverter<Assignment, String> {

	@Override
	public String convertToDatabaseColumn(Assignment assignment) {
		if (assignment == null) {
			return null;
		}
		return assignment.getCode();
	}

	@Override
	public Assignment convertToEntityAttribute(String code) {
		if (code == null) {
			return null;
		}
		return Assignment.of(code);
	}

}
