/**
 */
package org.eclipse.ice.docker.api.tests;

import junit.framework.TestCase;

import junit.textui.TestRunner;

import org.eclipse.ice.docker.api.ContainerConfiguration;
import org.eclipse.ice.docker.api.DockerapiFactory;

/**
 * <!-- begin-user-doc -->
 * A test case for the model object '<em><b>Container Configuration</b></em>'.
 * <!-- end-user-doc -->
 * @generated
 */
public class ContainerConfigurationTest extends TestCase {

	/**
	 * The fixture for this Container Configuration test case.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ContainerConfiguration fixture = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static void main(String[] args) {
		TestRunner.run(ContainerConfigurationTest.class);
	}

	/**
	 * Constructs a new Container Configuration test case with the given name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ContainerConfigurationTest(String name) {
		super(name);
	}

	/**
	 * Sets the fixture for this Container Configuration test case.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void setFixture(ContainerConfiguration fixture) {
		this.fixture = fixture;
	}

	/**
	 * Returns the fixture for this Container Configuration test case.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ContainerConfiguration getFixture() {
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
		setFixture(DockerapiFactory.eINSTANCE.createContainerConfiguration());
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

} //ContainerConfigurationTest
