/**
 * 
 */
package org.attendantsoffice.eventmanager.assignment;

import static org.junit.Assert.assertEquals;

import org.attendantsoffice.eventmanager.event.assigment.Assignment;
import org.attendantsoffice.eventmanager.user.UserPosition;
import org.junit.Test;

/**
 * Test the {@code Assignment}
 */
public class AssignmentTest {

	@Test
	public void testOf() {
		assertEquals(Assignment.ASSISTANT_OVERSEER, Assignment.of(Assignment.ASSISTANT_OVERSEER.getCode()));
	}
	
	@Test
	public void testElderOnlyApplicableRole() {
		// an elder-only assignment
		assertEquals(true, Assignment.ATTENDANT_OVERSEER.isApplicableRole(UserPosition.ELDER));
		assertEquals(false, Assignment.ATTENDANT_OVERSEER.isApplicableRole(UserPosition.MS));
		assertEquals(false, Assignment.ATTENDANT_OVERSEER.isApplicableRole(UserPosition.BAPTISEDBRO));
		assertEquals(false, Assignment.ATTENDANT_OVERSEER.isApplicableRole(UserPosition.BAPTISEDSIS));
	}
	
	@Test
	public void testSisterOnlyApplicableRole() {
		// an elder-only assignment
		assertEquals(false, Assignment.CARE_ASSISTANT.isApplicableRole(UserPosition.ELDER));
		assertEquals(false, Assignment.CARE_ASSISTANT.isApplicableRole(UserPosition.MS));
		assertEquals(false, Assignment.CARE_ASSISTANT.isApplicableRole(UserPosition.BAPTISEDBRO));
		assertEquals(true, Assignment.CARE_ASSISTANT.isApplicableRole(UserPosition.BAPTISEDSIS));
	}
	
	@Test
	public void testBrotherOnlyApplicableRole() {
		// an elder-only assignment
		assertEquals(true, Assignment.ATTENDANT.isApplicableRole(UserPosition.ELDER));
		assertEquals(true, Assignment.ATTENDANT.isApplicableRole(UserPosition.MS));
		assertEquals(true, Assignment.ATTENDANT.isApplicableRole(UserPosition.BAPTISEDBRO));
		assertEquals(false, Assignment.ATTENDANT.isApplicableRole(UserPosition.BAPTISEDSIS));
	}
	
	@Test
	public void testUnrestrictedApplicableRole() {
		// an elder-only assignment
		assertEquals(true, Assignment.OFFICE_SUPPORT.isApplicableRole(UserPosition.ELDER));
		assertEquals(true, Assignment.OFFICE_SUPPORT.isApplicableRole(UserPosition.MS));
		assertEquals(true, Assignment.OFFICE_SUPPORT.isApplicableRole(UserPosition.BAPTISEDBRO));
		assertEquals(true, Assignment.OFFICE_SUPPORT.isApplicableRole(UserPosition.BAPTISEDSIS));
	}
}
