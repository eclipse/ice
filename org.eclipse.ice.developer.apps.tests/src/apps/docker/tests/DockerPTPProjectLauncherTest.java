/**
 */
package apps.docker.tests;

import apps.docker.DockerFactory;
import apps.docker.DockerPTPProjectLauncher;

import junit.framework.TestCase;

import junit.textui.TestRunner;

/**
 * <!-- begin-user-doc -->
 * A test case for the model object '<em><b>PTP Project Launcher</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following operations are tested:
 * <ul>
 *   <li>{@link apps.docker.DockerPTPProjectLauncher#launchProject(apps.SourcePackage) <em>Launch Project</em>}</li>
 * </ul>
 * </p>
 * @generated
 */
public class DockerPTPProjectLauncherTest extends TestCase {

	/**
	 * The fixture for this PTP Project Launcher test case.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected DockerPTPProjectLauncher fixture = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static void main(String[] args) {
		TestRunner.run(DockerPTPProjectLauncherTest.class);
	}

	/**
	 * Constructs a new PTP Project Launcher test case with the given name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DockerPTPProjectLauncherTest(String name) {
		super(name);
	}

	/**
	 * Sets the fixture for this PTP Project Launcher test case.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void setFixture(DockerPTPProjectLauncher fixture) {
		this.fixture = fixture;
	}

	/**
	 * Returns the fixture for this PTP Project Launcher test case.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected DockerPTPProjectLauncher getFixture() {
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
		setFixture(DockerFactory.eINSTANCE.createDockerPTPProjectLauncher());
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
	 * Tests the '{@link apps.docker.DockerPTPProjectLauncher#launchProject(apps.SourcePackage) <em>Launch Project</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see apps.docker.DockerPTPProjectLauncher#launchProject(apps.SourcePackage)
	 */
	public void testLaunchProject__SourcePackage() {
		// TODO: implement this operation test method
		// Ensure that you remove @generated or mark it @generated NOT
//		fail();
	}

} //DockerPTPProjectLauncherTest
