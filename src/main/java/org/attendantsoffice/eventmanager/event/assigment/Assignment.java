package org.attendantsoffice.eventmanager.event.assigment;

import java.util.EnumSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.attendantsoffice.eventmanager.user.UserPosition;

import com.google.common.collect.Sets;

/**
 * The type of assignment that a user can have in an event team. 
 * These values are also in the database, but only for data integrity
 */
public enum Assignment {
	ATTENDANT_OVERSEER("AOVRR", "Attendants Overseer", Restrictions.ELDER),
	ASSISTANT_OVERSEER("AAOVR", "Asst Overseer", Restrictions.ELDER),
	DIVISION_CAPTAIN("DCAPN", "Division Captain", Restrictions.ELDER),
	ASSISTANT_DIVISION_CAPTAIN("ADCPN", "Asst Division Captain", Restrictions.BROTHER),
	TEAM_CAPTAIN("TCAPN", "Team Captain", Restrictions.ELDER),
	ATTENDANT("ATNDT", "Attendant", Restrictions.BROTHER),
	CARE_ASSISTANT("CRAST", "Care Assistant", Restrictions.SISTER),
	REGISTRATION_OVERSEER("ROVRR", "Registration Overseer", Restrictions.ELDER),
	ASSISTANT_REGISTRATION_OVERSEER("AROVR", "Asst Registration Overseer", Restrictions.BROTHER),
	OFFICE_SUPPORT("OFSPT", "Office Support", Restrictions.UNRESTRICTED),
	OBSA_INCIDENT_CONTROLLER("IICTR", "IBSA Incident Controller", Restrictions.BROTHER),
	PMR_OVERSEER("POVRR", "PMR Overseer", Restrictions.ELDER),
	ASSISTANT_PMR_OVERSEER("APOVR", "Asst PMR Overseer", Restrictions.BROTHER);
	
	private final String code;
	private final String name;
	private final Set<UserPosition> restrictedPositions;
	
	private static final Map<String, Assignment> ASSIGNMENTS_MAP = Stream.of(values())
			.collect(Collectors.toMap(Assignment::getCode, Function.identity()));
	
	/**
	 * @param code code stored in the database
	 * @param name displayable name of the assignment
	 */
	private Assignment(String code, String name, Set<UserPosition> restrictedPositions) {
		this.code = code;
		this.name = name;
		this.restrictedPositions = restrictedPositions;
	}
	
	public static Assignment of(String code) {
		return ASSIGNMENTS_MAP.get(code);
	}
	
	/**
	 * @return true if the assignment is available to the userm defined by their position
	 */
	public boolean isApplicableRole(UserPosition userPosition) {
		return restrictedPositions.contains(userPosition);
	}
	
	public String getCode() {
		return code;
	}

	public String getName() {
		return name;
	}
	
	private static class Restrictions {
		static final Set<UserPosition> UNRESTRICTED = EnumSet.allOf(UserPosition.class);
		static final Set<UserPosition> SISTER = Sets.newHashSet(UserPosition.BAPTISEDSIS);
		static final Set<UserPosition> BROTHER = Sets.newHashSet(UserPosition.BAPTISEDBRO, UserPosition.MS, UserPosition.ELDER);
		static final Set<UserPosition> ELDER = Sets.newHashSet(UserPosition.ELDER);
	}
}
