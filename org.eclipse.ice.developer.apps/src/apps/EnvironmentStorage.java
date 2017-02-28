/**
 */
package apps;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Environment Storage</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * The EnvironmentStorage interface provides a mechanism for persisting and loading environments. 
 * <!-- end-model-doc -->
 *
 *
 * @see apps.AppsPackage#getEnvironmentStorage()
 * @model interface="true" abstract="true"
 * @generated
 */
public interface EnvironmentStorage extends EObject {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model environmentsMany="false"
	 */
	void store(EList<IEnvironment> environments);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model
	 * @generated
	 */
	EList<IEnvironment> load();

} // EnvironmentStorage
