/**
 */
package apps.docker;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see apps.docker.DockerPackage
 * @generated
 */
public interface DockerFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	DockerFactory eINSTANCE = apps.docker.impl.DockerFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>Environment</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Environment</em>'.
	 * @generated
	 */
	DockerEnvironment createDockerEnvironment();

	/**
	 * Returns a new object of class '<em>API</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>API</em>'.
	 * @generated
	 */
	DockerAPI createDockerAPI();

	/**
	 * Returns a new object of class '<em>Container Configuration</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Container Configuration</em>'.
	 * @generated
	 */
	ContainerConfiguration createContainerConfiguration();

	/**
	 * Returns a new object of class '<em>PTP Project Launcher</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>PTP Project Launcher</em>'.
	 * @generated
	 */
	DockerPTPProjectLauncher createDockerPTPProjectLauncher();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	DockerPackage getDockerPackage();

} //DockerFactory
