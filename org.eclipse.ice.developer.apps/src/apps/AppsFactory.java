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
	 * Returns a new object of class '<em>Environment</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Environment</em>'.
	 * @generated
	 */
	Environment createEnvironment();

	/**
	 * Returns a new object of class '<em>PTP Sync Project Launcher</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>PTP Sync Project Launcher</em>'.
	 * @generated
	 */
	PTPSyncProjectLauncher createPTPSyncProjectLauncher();

	/**
	 * Returns a new object of class '<em>Local CDT Project Launcher</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Local CDT Project Launcher</em>'.
	 * @generated
	 */
	LocalCDTProjectLauncher createLocalCDTProjectLauncher();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	AppsPackage getAppsPackage();

} //AppsFactory
