/**
 */
package apps.docker.tests;

import apps.docker.DockerFactory;
import apps.docker.DockerProjectLauncher;

import junit.framework.TestCase;

import junit.textui.TestRunner;

/**
 * <!-- begin-user-doc -->
 * A test case for the model object '<em><b>Project Launcher</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following operations are tested:
 * <ul>
 *   <li>{@link apps.docker.DockerProjectLauncher#launchProject(apps.SourcePackage) <em>Launch Project</em>}</li>
 *   <li>{@link apps.docker.DockerProjectLauncher#updateConnection(int) <em>Update Connection</em>}</li>
 * </ul>
 * </p>
 * @generated
 */
public class DockerProjectLauncherTest extends TestCase {

	/**
	 * The fixture for this Project Launcher test case.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected DockerProjectLauncher fixture = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static void main(String[] args) {
		TestRunner.run(DockerProjectLauncherTest.class);
	}

	/**
	 * Constructs a new Project Launcher test case with the given name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DockerProjectLauncherTest(String name) {
		super(name);
	}

	/**
	 * Sets the fixture for this Project Launcher test case.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void setFixture(DockerProjectLauncher fixture) {
		this.fixture = fixture;
	}

	/**
	 * Returns the fixture for this Project Launcher test case.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected DockerProjectLauncher getFixture() {
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
		setFixture(DockerFactory.eINSTANCE.createDockerProjectLauncher());
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
	 * Tests the '{@link apps.docker.DockerProjectLauncher#launchProject(apps.SourcePackage) <em>Launch Project</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see apps.docker.DockerProjectLauncher#launchProject(apps.SourcePackage)
	 */
	public void testLaunchProject__SourcePackage() {
		// TODO: implement this operation test method
		// Ensure that you remove @generated or mark it @generated NOT
		assertTrue(true);// nothing really to do here
	}

	/**
	 * Tests the '{@link apps.docker.DockerProjectLauncher#updateConnection(int) <em>Update Connection</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see apps.docker.DockerProjectLauncher#updateConnection(int)
	 */
	public void testUpdateConnection__int() {
		// TODO: implement this operation test method
		// Ensure that you remove @generated or mark it @generated NOT
		assertTrue(true);
	}

} //DockerProjectLauncherTest
