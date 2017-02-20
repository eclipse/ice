/**
 */
package apps.tests;

import apps.EnvironmentFactory;
import apps.SpackPackage;

import junit.framework.TestCase;

import junit.textui.TestRunner;

/**
 * <!-- begin-user-doc -->
 * A test case for the model object '<em><b>Spack Package</b></em>'.
 * <!-- end-user-doc -->
 * @generated
 */
public class SpackPackageTest extends TestCase {

	/**
	 * The fixture for this Spack Package test case.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected SpackPackage fixture = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static void main(String[] args) {
		TestRunner.run(SpackPackageTest.class);
	}

	/**
	 * Constructs a new Spack Package test case with the given name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SpackPackageTest(String name) {
		super(name);
	}

	/**
	 * Sets the fixture for this Spack Package test case.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void setFixture(SpackPackage fixture) {
		this.fixture = fixture;
	}

	/**
	 * Returns the fixture for this Spack Package test case.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected SpackPackage getFixture() {
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
		setFixture(EnvironmentFactory.eINSTANCE.createSpackPackage());
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

} //SpackPackageTest
