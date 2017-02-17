/**
 */
package org.eclipse.ice.developer.scenvironment.model.scenvironment.tests;

import junit.textui.TestRunner;

import org.eclipse.ice.developer.scenvironment.model.scenvironment.FileSystemConfiguration;
import org.eclipse.ice.developer.scenvironment.model.scenvironment.ScenvironmentFactory;

/**
 * <!-- begin-user-doc -->
 * A test case for the model object '<em><b>File System Configuration</b></em>'.
 * <!-- end-user-doc -->
 * @generated
 */
public class FileSystemConfigurationTest extends InstallerTypeConfigurationTest {

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static void main(String[] args) {
		TestRunner.run(FileSystemConfigurationTest.class);
	}

	/**
	 * Constructs a new File System Configuration test case with the given name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public FileSystemConfigurationTest(String name) {
		super(name);
	}

	/**
	 * Returns the fixture for this File System Configuration test case.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected FileSystemConfiguration getFixture() {
		return (FileSystemConfiguration)fixture;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see junit.framework.TestCase#setUp()
	 * @generated
	 */
	@Override
	protected void setUp() throws Exception {
		setFixture(ScenvironmentFactory.eINSTANCE.createFileSystemConfiguration());
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

} //FileSystemConfigurationTest
