/**
 */
package apps.eclipse.tests;

import apps.docker.tests.DockerProjectLauncherTest;

import apps.eclipse.DockerPTPSyncProjectLauncher;
import apps.eclipse.EclipseFactory;

import junit.textui.TestRunner;

/**
 * <!-- begin-user-doc -->
 * A test case for the model object '<em><b>Docker PTP Sync Project Launcher</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following operations are tested:
 * <ul>
 *   <li>{@link apps.eclipse.DockerPTPSyncProjectLauncher#launchProject(apps.SourcePackage) <em>Launch Project</em>}</li>
 * </ul>
 * </p>
 * @generated
 */
public class DockerPTPSyncProjectLauncherTest extends DockerProjectLauncherTest {

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static void main(String[] args) {
		TestRunner.run(DockerPTPSyncProjectLauncherTest.class);
	}

	/**
	 * Constructs a new Docker PTP Sync Project Launcher test case with the given name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DockerPTPSyncProjectLauncherTest(String name) {
		super(name);
	}

	/**
	 * Returns the fixture for this Docker PTP Sync Project Launcher test case.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected DockerPTPSyncProjectLauncher getFixture() {
		return (DockerPTPSyncProjectLauncher)fixture;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see junit.framework.TestCase#setUp()
	 * @generated
	 */
	@Override
	protected void setUp() throws Exception {
		setFixture(EclipseFactory.eINSTANCE.createDockerPTPSyncProjectLauncher());
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
	 * Tests the '{@link apps.eclipse.DockerPTPSyncProjectLauncher#launchProject(apps.SourcePackage) <em>Launch Project</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see apps.eclipse.DockerPTPSyncProjectLauncher#launchProject(apps.SourcePackage)
	 * @generated
	 */
	public void testLaunchProject__SourcePackage() {
		// TODO: implement this operation test method
		// Ensure that you remove @generated or mark it @generated NOT
		fail();
	}

	public void testDummy() {
		assertTrue(true);
	}

} //DockerPTPSyncProjectLauncherTest
