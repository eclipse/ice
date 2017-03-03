/**
 */
package eclipseapps.tests;

import junit.framework.Test;
import junit.framework.TestSuite;

import junit.textui.TestRunner;

/**
 * <!-- begin-user-doc -->
 * A test suite for the '<em><b>eclipseapps</b></em>' package.
 * <!-- end-user-doc -->
 * @generated
 */
public class EclipseappsTests extends TestSuite {

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
		TestSuite suite = new EclipseappsTests("eclipseapps Tests");
		suite.addTestSuite(EclipseEnvironmentStorageTest.class);
		suite.addTestSuite(DockerPTPSyncProjectLauncherTest.class);
		suite.addTestSuite(EclipseCppProjectProviderTest.class);
		return suite;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EclipseappsTests(String name) {
		super(name);
	}

} //EclipseappsTests
