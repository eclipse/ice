/**
 */
package org.eclipse.ice.developer.scenvironment.model.scenvironment.tests;

import junit.framework.Test;
import junit.framework.TestSuite;

import junit.textui.TestRunner;

/**
 * <!-- begin-user-doc -->
 * A test suite for the '<em><b>scenvironment</b></em>' package.
 * <!-- end-user-doc -->
 * @generated
 */
public class ScenvironmentTests extends TestSuite {

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static void main(String[] args) {
		TestRunner.run(suite());
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static Test suite() {
		TestSuite suite = new ScenvironmentTests("scenvironment Tests");
		suite.addTestSuite(SCEnvironmentTest.class);
		suite.addTestSuite(DockerInstallerTest.class);
		suite.addTestSuite(FileSystemInstallerTest.class);
		suite.addTestSuite(SCEnvironmentDataManagerTest.class);
		return suite;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ScenvironmentTests(String name) {
		super(name);
	}

} //ScenvironmentTests
