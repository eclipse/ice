/**
 */
package org.eclipse.ice.docker.api.tests;

import junit.framework.Test;
import junit.framework.TestSuite;

import junit.textui.TestRunner;

import org.eclipse.ice.docker.api.spotify.tests.SpotifyTests;

/**
 * <!-- begin-user-doc -->
 * A test suite for the '<em><b>Org.eclipse.ice</b></em>' model.
 * <!-- end-user-doc -->
 * @generated
 */
public class OrgeclipseiceAllTests extends TestSuite {

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static void main(String[] args) {
		TestRunner.run(suite());
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static Test suite() {
		TestSuite suite = new OrgeclipseiceAllTests("Org.eclipse.ice Tests");
		suite.addTest(DockerapiTests.suite());
		suite.addTest(SpotifyTests.suite());
		return suite;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public OrgeclipseiceAllTests(String name) {
		super(name);
	}

} //OrgeclipseiceAllTests
