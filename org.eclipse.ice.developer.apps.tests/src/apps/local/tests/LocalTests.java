/**
 */
package apps.local.tests;

import junit.framework.Test;
import junit.framework.TestSuite;

import junit.textui.TestRunner;

/**
 * <!-- begin-user-doc -->
 * A test suite for the '<em><b>local</b></em>' package.
 * <!-- end-user-doc -->
 * @generated
 */
public class LocalTests extends TestSuite {

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
		TestSuite suite = new LocalTests("local Tests");
		suite.addTestSuite(LocalEnvironmentBuilderTest.class);
		suite.addTestSuite(LocalEnvironmentTest.class);
		return suite;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public LocalTests(String name) {
		super(name);
	}

} //LocalTests
