/**
 */
package apps.docker.tests;

import junit.framework.Test;
import junit.framework.TestSuite;

import junit.textui.TestRunner;

/**
 * <!-- begin-user-doc -->
 * A test suite for the '<em><b>docker</b></em>' package.
 * <!-- end-user-doc -->
 * @generated
 */
public class DockerTests extends TestSuite {

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
		TestSuite suite = new DockerTests("docker Tests");
		suite.addTestSuite(DockerEnvironmentTest.class);
		suite.addTestSuite(DockerAPITest.class);
		return suite;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DockerTests(String name) {
		super(name);
	}

} //DockerTests
