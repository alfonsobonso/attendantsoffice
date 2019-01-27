/**
 * 
 */
package org.attendantsoffice.eventmanager.event.assigment;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class AssignmentStatusConverter implements AttributeConverter<AssignmentStatus, String> {

	@Override
	public String convertToDatabaseColumn(AssignmentStatus assignmentStatus) {
		if (assignmentStatus == null) {
			return null;
		}
		return assignmentStatus.getCode();
	}

	@Override
	public AssignmentStatus convertToEntityAttribute(String code) {
		if (code == null) {
			return null;
		}
		return AssignmentStatus.of(code);
	}

}
