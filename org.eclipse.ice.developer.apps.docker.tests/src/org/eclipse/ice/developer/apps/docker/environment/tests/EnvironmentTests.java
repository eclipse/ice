/**
 */
package org.eclipse.ice.developer.apps.docker.environment.tests;

import junit.framework.Test;
import junit.framework.TestSuite;

import junit.textui.TestRunner;

/**
 * <!-- begin-user-doc -->
 * A test suite for the '<em><b>environment</b></em>' package.
 * <!-- end-user-doc -->
 * @generated
 */
public class EnvironmentTests extends TestSuite {

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
		TestSuite suite = new EnvironmentTests("environment Tests");
		suite.addTestSuite(DockerEnvironmentTest.class);
		suite.addTestSuite(DockerProjectLauncherTest.class);
		suite.addTestSuite(DockerEnvironmentBuilderTest.class);
		return suite;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EnvironmentTests(String name) {
		super(name);
	}

} //EnvironmentTests
