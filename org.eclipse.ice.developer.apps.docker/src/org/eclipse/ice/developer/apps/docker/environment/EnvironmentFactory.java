/**
 */
package org.eclipse.ice.developer.apps.docker.environment;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see org.eclipse.ice.developer.apps.docker.environment.EnvironmentPackage
 * @generated
 */
public interface EnvironmentFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	EnvironmentFactory eINSTANCE = org.eclipse.ice.developer.apps.docker.environment.impl.EnvironmentFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>Docker Environment</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Docker Environment</em>'.
	 * @generated
	 */
	DockerEnvironment createDockerEnvironment();

	/**
	 * Returns a new object of class '<em>Docker Project Launcher</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Docker Project Launcher</em>'.
	 * @generated
	 */
	DockerProjectLauncher createDockerProjectLauncher();

	/**
	 * Returns a new object of class '<em>Docker Environment Builder</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Docker Environment Builder</em>'.
	 * @generated
	 */
	DockerEnvironmentBuilder createDockerEnvironmentBuilder();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	EnvironmentPackage getEnvironmentPackage();

} //EnvironmentFactory
