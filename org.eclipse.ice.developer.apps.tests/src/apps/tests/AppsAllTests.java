/**
 */
package apps.tests;

import junit.framework.Test;
import junit.framework.TestSuite;

import junit.textui.TestRunner;

/**
 * <!-- begin-user-doc -->
 * A test suite for the '<em><b>Apps</b></em>' model.
 * <!-- end-user-doc -->
 * @generated
 */
public class AppsAllTests extends TestSuite {

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
	 */
	public static Test suite() {
		TestSuite suite = new AppsAllTests("Apps Tests");
		suite.addTest(AppsTests.suite());
//		suite.addTest(LocalTests.suite());
		return suite;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AppsAllTests(String name) {
		super(name);
	}

} //AppsAllTests
