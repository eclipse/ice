/**
 */
package apps;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see apps.AppsPackage
 * @generated
 */
public interface AppsFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	AppsFactory eINSTANCE = apps.impl.AppsFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>Environment Manager</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Environment Manager</em>'.
	 * @generated
	 */
	EnvironmentManager createEnvironmentManager();

	/**
	 * Returns a new object of class '<em>Spack Package</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Spack Package</em>'.
	 * @generated
	 */
	SpackPackage createSpackPackage();

	/**
	 * Returns a new object of class '<em>Source Package</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Source Package</em>'.
	 * @generated
	 */
	SourcePackage createSourcePackage();

	/**
	 * Returns a new object of class '<em>OS Package</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>OS Package</em>'.
	 * @generated
	 */
	OSPackage createOSPackage();

	/**
	 * Returns a new object of class '<em>Spack Dependency</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Spack Dependency</em>'.
	 * @generated
	 */
	SpackDependency createSpackDependency();

	/**
	 * Returns a new object of class '<em>Json Environment Creator</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Json Environment Creator</em>'.
	 * @generated
	 */
	JsonEnvironmentCreator createJsonEnvironmentCreator();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	AppsPackage getAppsPackage();

} //AppsFactory
