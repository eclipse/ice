/**
 */
package apps.tests;

import apps.docker.tests.DockerTests;

import apps.local.tests.LocalTests;

import junit.framework.Test;
import junit.framework.TestSuite;

import junit.textui.TestRunner;

/**
 * <!-- begin-user-doc -->
 * A test suite for the '<em><b>Environment</b></em>' model.
 * <!-- end-user-doc -->
 * @generated
 */
public class EnvironmentAllTests extends TestSuite {

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
		TestSuite suite = new EnvironmentAllTests("Environment Tests");
		suite.addTest(EnvironmentTests.suite());
		suite.addTest(DockerTests.suite());
		suite.addTest(LocalTests.suite());
		return suite;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EnvironmentAllTests(String name) {
		super(name);
	}

} //EnvironmentAllTests
