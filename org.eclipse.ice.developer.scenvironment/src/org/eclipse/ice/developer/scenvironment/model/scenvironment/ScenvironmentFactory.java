/**
 */
package org.eclipse.ice.developer.scenvironment.model.scenvironment;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see org.eclipse.ice.developer.scenvironment.model.scenvironment.ScenvironmentPackage
 * @generated
 */
public interface ScenvironmentFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	ScenvironmentFactory eINSTANCE = org.eclipse.ice.developer.scenvironment.model.scenvironment.impl.ScenvironmentFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>SC Environment</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>SC Environment</em>'.
	 * @generated
	 */
	SCEnvironment createSCEnvironment();

	/**
	 * Returns a new object of class '<em>Spack Package</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Spack Package</em>'.
	 * @generated
	 */
	SpackPackage createSpackPackage();

	/**
	 * Returns a new object of class '<em>Container Configuration</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Container Configuration</em>'.
	 * @generated
	 */
	ContainerConfiguration createContainerConfiguration();

	/**
	 * Returns a new object of class '<em>Docker Installer</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Docker Installer</em>'.
	 * @generated
	 */
	DockerInstaller createDockerInstaller();

	/**
	 * Returns a new object of class '<em>File System Installer</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>File System Installer</em>'.
	 * @generated
	 */
	FileSystemInstaller createFileSystemInstaller();

	/**
	 * Returns a new object of class '<em>SC Environment Data Manager</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>SC Environment Data Manager</em>'.
	 * @generated
	 */
	SCEnvironmentDataManager createSCEnvironmentDataManager();

	/**
	 * Returns a new object of class '<em>File System Configuration</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>File System Configuration</em>'.
	 * @generated
	 */
	FileSystemConfiguration createFileSystemConfiguration();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	ScenvironmentPackage getScenvironmentPackage();

} //ScenvironmentFactory
