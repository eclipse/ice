/**
 */
package org.eclipse.ice.docker.api.spotify.tests;

import junit.framework.Test;
import junit.framework.TestSuite;

import junit.textui.TestRunner;

/**
 * <!-- begin-user-doc -->
 * A test suite for the '<em><b>spotify</b></em>' package.
 * <!-- end-user-doc -->
 * @generated
 */
public class SpotifyTests extends TestSuite {

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
		TestSuite suite = new SpotifyTests("spotify Tests");
		suite.addTestSuite(SpotifyDockerClientTest.class);
		return suite;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SpotifyTests(String name) {
		super(name);
	}

} //SpotifyTests
