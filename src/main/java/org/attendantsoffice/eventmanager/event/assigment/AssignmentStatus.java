package org.attendantsoffice.eventmanager.event.assigment;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * There is a 2-step process before a volunteer can begin the process (including training) or taking up an assignment.
 * For any given event, all assignments are initially pending approval. At that point, the approver may say that they will not be 
 * available for any assignment, which would be reflected in their user status. 
 * Alternatively, they may be approved, but later checking with the volunteer themselves indicates that they would not be in a 
 * position to take up the assignment.
 * We don't explicitly capture an unapproved status in an attempt to prevent any embarrassment. This does lead to a slight fuzziness 
 * around these definitions.
 * @see {@code org.attendantsoffice.eventmanager.user.UserStatus}
 */
public enum AssignmentStatus {

	/**
	 * The assignment is pending approval, generally from a congregation representative
	 */
	PENDING_APPROVAL("PND"), 
	
	/**
	 * For some reason, either because the volunteer is not available, or because of the approval check, 
	 * the volunteer will not be able to take up their assignment. 
	 * We do not specify the cause here to prevent any further details being implied.
	 * @see {@code org.attendantsoffice.eventmanager.user.UserStatus}
	 */
	UNAVAILABLE("AAV"),
	
	/**
	 * The volunteer has been approved and, as far as we know at the moment, is available.
	 */
	AVAILABLE("AVL");
	
	private static final Map<String, AssignmentStatus> ASSIGNMENT_STATUS_MAP = Stream.of(values())
			.collect(Collectors.toMap(AssignmentStatus::getCode, Function.identity()));
	
	private final String code;

	private AssignmentStatus(String code) {
		this.code = code;
	}

	public static AssignmentStatus of(String code) {
		return ASSIGNMENT_STATUS_MAP.get(code);
	}
	
	public String getCode() {
		return code;
	}
	
}
