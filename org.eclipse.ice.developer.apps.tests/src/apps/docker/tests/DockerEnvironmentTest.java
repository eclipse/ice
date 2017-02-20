/**
 */
package apps.docker.tests;

import apps.docker.DockerEnvironment;
import apps.docker.DockerFactory;

import apps.tests.EnvironmentTest;

import junit.textui.TestRunner;

/**
 * <!-- begin-user-doc -->
 * A test case for the model object '<em><b>Environment</b></em>'.
 * <!-- end-user-doc -->
 * @generated
 */
public class DockerEnvironmentTest extends EnvironmentTest {

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static void main(String[] args) {
		TestRunner.run(DockerEnvironmentTest.class);
	}

	/**
	 * Constructs a new Environment test case with the given name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DockerEnvironmentTest(String name) {
		super(name);
	}

	/**
	 * Returns the fixture for this Environment test case.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected DockerEnvironment getFixture() {
		return (DockerEnvironment)fixture;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see junit.framework.TestCase#setUp()
	 * @generated
	 */
	@Override
	protected void setUp() throws Exception {
		setFixture(DockerFactory.eINSTANCE.createDockerEnvironment());
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see junit.framework.TestCase#tearDown()
	 * @generated
	 */
	@Override
	protected void tearDown() throws Exception {
		setFixture(null);
	}

} //DockerEnvironmentTest
