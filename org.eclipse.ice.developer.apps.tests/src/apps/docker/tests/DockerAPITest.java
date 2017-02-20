/**
 */
package apps.docker.tests;

import apps.docker.DockerAPI;
import apps.docker.DockerFactory;

import junit.framework.TestCase;

import junit.textui.TestRunner;

/**
 * <!-- begin-user-doc -->
 * A test case for the model object '<em><b>API</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following operations are tested:
 * <ul>
 *   <li>{@link apps.docker.DockerAPI#buildImage(java.lang.String) <em>Build Image</em>}</li>
 *   <li>{@link apps.docker.DockerAPI#launchContainer(java.lang.String, apps.docker.ContainerConfiguration) <em>Launch Container</em>}</li>
 * </ul>
 * </p>
 * @generated
 */
public class DockerAPITest extends TestCase {

	/**
	 * The fixture for this API test case.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected DockerAPI fixture = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static void main(String[] args) {
		TestRunner.run(DockerAPITest.class);
	}

	/**
	 * Constructs a new API test case with the given name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DockerAPITest(String name) {
		super(name);
	}

	/**
	 * Sets the fixture for this API test case.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void setFixture(DockerAPI fixture) {
		this.fixture = fixture;
	}

	/**
	 * Returns the fixture for this API test case.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected DockerAPI getFixture() {
		return fixture;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see junit.framework.TestCase#setUp()
	 * @generated
	 */
	@Override
	protected void setUp() throws Exception {
		setFixture(DockerFactory.eINSTANCE.createDockerAPI());
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

	/**
	 * Tests the '{@link apps.docker.DockerAPI#buildImage(java.lang.String) <em>Build Image</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see apps.docker.DockerAPI#buildImage(java.lang.String)
	 * @generated
	 */
	public void testBuildImage__String() {
		// TODO: implement this operation test method
		// Ensure that you remove @generated or mark it @generated NOT
		fail();
	}

	/**
	 * Tests the '{@link apps.docker.DockerAPI#launchContainer(java.lang.String, apps.docker.ContainerConfiguration) <em>Launch Container</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see apps.docker.DockerAPI#launchContainer(java.lang.String, apps.docker.ContainerConfiguration)
	 * @generated
	 */
	public void testLaunchContainer__String_ContainerConfiguration() {
		// TODO: implement this operation test method
		// Ensure that you remove @generated or mark it @generated NOT
		fail();
	}

} //DockerAPITest
