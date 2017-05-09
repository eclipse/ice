/**
 */
package org.eclipse.ice.docker.api.tests;

import junit.framework.TestCase;

import junit.textui.TestRunner;

import org.eclipse.ice.docker.api.DockerapiFactory;
import org.eclipse.ice.docker.api.StdOutConsole;

/**
 * <!-- begin-user-doc -->
 * A test case for the model object '<em><b>Std Out Console</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following operations are tested:
 * <ul>
 *   <li>{@link org.eclipse.ice.docker.api.DockerMessageConsole#print(java.lang.String) <em>Print</em>}</li>
 * </ul>
 * </p>
 * @generated
 */
public class StdOutConsoleTest extends TestCase {

	/**
	 * The fixture for this Std Out Console test case.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected StdOutConsole fixture = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static void main(String[] args) {
		TestRunner.run(StdOutConsoleTest.class);
	}

	/**
	 * Constructs a new Std Out Console test case with the given name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public StdOutConsoleTest(String name) {
		super(name);
	}

	/**
	 * Sets the fixture for this Std Out Console test case.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void setFixture(StdOutConsole fixture) {
		this.fixture = fixture;
	}

	/**
	 * Returns the fixture for this Std Out Console test case.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected StdOutConsole getFixture() {
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
		setFixture(DockerapiFactory.eINSTANCE.createStdOutConsole());
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
	 * Tests the '{@link org.eclipse.ice.docker.api.DockerMessageConsole#print(java.lang.String) <em>Print</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.ice.docker.api.DockerMessageConsole#print(java.lang.String)
	 * @generated
	 */
	public void testPrint__String() {
		// TODO: implement this operation test method
		// Ensure that you remove @generated or mark it @generated NOT
		fail();
	}

} //StdOutConsoleTest
