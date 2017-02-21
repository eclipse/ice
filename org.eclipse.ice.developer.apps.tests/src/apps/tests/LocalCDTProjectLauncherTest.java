/**
 */
package apps.tests;

import org.junit.Ignore;

import apps.AppsFactory;
import apps.LocalCDTProjectLauncher;

import junit.framework.TestCase;

import junit.textui.TestRunner;

/**
 * <!-- begin-user-doc -->
 * A test case for the model object '<em><b>Local CDT Project Launcher</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following operations are tested:
 * <ul>
 *   <li>{@link apps.LocalCDTProjectLauncher#launchProject(apps.SpackPackage) <em>Launch Project</em>}</li>
 * </ul>
 * </p>
 * @generated
 */
public class LocalCDTProjectLauncherTest extends TestCase {

	/**
	 * The fixture for this Local CDT Project Launcher test case.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected LocalCDTProjectLauncher fixture = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static void main(String[] args) {
		TestRunner.run(LocalCDTProjectLauncherTest.class);
	}

	/**
	 * Constructs a new Local CDT Project Launcher test case with the given name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public LocalCDTProjectLauncherTest(String name) {
		super(name);
	}

	/**
	 * Sets the fixture for this Local CDT Project Launcher test case.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void setFixture(LocalCDTProjectLauncher fixture) {
		this.fixture = fixture;
	}

	/**
	 * Returns the fixture for this Local CDT Project Launcher test case.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected LocalCDTProjectLauncher getFixture() {
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
		setFixture(AppsFactory.eINSTANCE.createLocalCDTProjectLauncher());
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
	 * Tests the '{@link apps.LocalCDTProjectLauncher#launchProject(apps.SpackPackage) <em>Launch Project</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see apps.LocalCDTProjectLauncher#launchProject(apps.SpackPackage)
	 */
	@Ignore
	public void testLaunchProject__SpackPackage() {
		// TODO: implement this operation test method
		// Ensure that you remove @generated or mark it @generated NOT
//		fail();
	}

} //LocalCDTProjectLauncherTest
