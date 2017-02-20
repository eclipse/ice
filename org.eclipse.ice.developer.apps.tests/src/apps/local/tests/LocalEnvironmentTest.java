/**
 */
package apps.local.tests;

import apps.local.LocalEnvironment;
import apps.local.LocalFactory;

import junit.framework.TestCase;

import junit.textui.TestRunner;

/**
 * <!-- begin-user-doc -->
 * A test case for the model object '<em><b>Environment</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following operations are tested:
 * <ul>
 *   <li>{@link apps.IEnvironment#launch() <em>Launch</em>}</li>
 * </ul>
 * </p>
 * @generated
 */
public class LocalEnvironmentTest extends TestCase {

	/**
	 * The fixture for this Environment test case.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected LocalEnvironment fixture = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static void main(String[] args) {
		TestRunner.run(LocalEnvironmentTest.class);
	}

	/**
	 * Constructs a new Environment test case with the given name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public LocalEnvironmentTest(String name) {
		super(name);
	}

	/**
	 * Sets the fixture for this Environment test case.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void setFixture(LocalEnvironment fixture) {
		this.fixture = fixture;
	}

	/**
	 * Returns the fixture for this Environment test case.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected LocalEnvironment getFixture() {
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
		setFixture(LocalFactory.eINSTANCE.createLocalEnvironment());
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
	 * Tests the '{@link apps.IEnvironment#launch() <em>Launch</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see apps.IEnvironment#launch()
	 * @generated
	 */
	public void testLaunch() {
		// TODO: implement this operation test method
		// Ensure that you remove @generated or mark it @generated NOT
		fail();
	}

} //LocalEnvironmentTest
